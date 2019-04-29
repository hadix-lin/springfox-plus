window.onload = () => {
    const executeRequest = (req) =>
        ({fn, specActions, specSelectors, getConfigs, oas3Selectors}) => {
            let {pathName, method, operation} = req
            let {requestInterceptor, responseInterceptor} = getConfigs()

            let op = operation.toJS()

            // ensure that explicitly-included params are in the request

            if (op && op.parameters && op.parameters.length) {
                op.parameters
                    .filter(param => param && param.allowEmptyValue === true)
                    .forEach(param => {
                        if (specSelectors.parameterInclusionSettingFor([pathName, method], param.name, param.in)) {
                            req.parameters = req.parameters || {}
                            const paramValue = req.parameters[param.name]

                            // if the value is false or an empty Immutable iterable...
                            if (!paramValue || (paramValue && paramValue.size === 0)) {
                                // set it to empty string, so Swagger Client will treat it as
                                // present but empty.
                                req.parameters[param.name] = ""
                            }
                        }
                    })
            }

            // if url is relative, parseUrl makes it absolute by inferring from `window.location`
            req.contextUrl = specSelectors.url();

            if (op && op.operationId) {
                req.operationId = op.operationId
            } else if (op && pathName && method) {
                req.operationId = fn.opId(op, pathName, method)
            }

            if (specSelectors.isOAS3()) {
                const namespace = `${pathName}:${method}`

                req.server = oas3Selectors.selectedServer(namespace) || oas3Selectors.selectedServer()

                const namespaceVariables = oas3Selectors.serverVariables({
                    server: req.server,
                    namespace
                }).toJS()
                const globalVariables = oas3Selectors.serverVariables({server: req.server}).toJS()

                req.serverVariables = Object.keys(namespaceVariables).length ? namespaceVariables : globalVariables

                req.requestContentType = oas3Selectors.requestContentType(pathName, method)
                req.responseContentType = oas3Selectors.responseContentType(pathName, method) || "*/*"
                const requestBody = oas3Selectors.requestBodyValue(pathName, method)

                if (isJSONObject(requestBody)) {
                    req.requestBody = JSON.parse(requestBody)
                } else if (requestBody && requestBody.toJS) {
                    req.requestBody = requestBody.toJS()
                } else {
                    req.requestBody = requestBody
                }
            }


            // hsf方法
            if (req.spec.tags[0].name.startsWith("[HSF]")) {
                const argsTypes = [];
                const argsObjs = [];
                const pathName = req.pathName;
                const operationSpec = req.spec.paths[pathName];
                let hsfOperation = operationSpec.post;
                if (hsfOperation.parameters) {
                    hsfOperation.parameters
                        .filter(p => p.in !== 'header')
                        .forEach(p => argsTypes.push(p.description.substring(0, p.description.indexOf('<br/>'))));

                    for (const key in req.parameters) {
                        if (key.startsWith("header")) {
                            continue;
                        }
                        argsObjs.push(JSON.parse(req.parameters[key]))
                    }
                }
                hsfOperation.parameters = [{
                    in: "body",
                    required: true,
                    name: "body",
                    schema: {type: "object"}
                }, {
                    default: "JsonContent",
                    enum: ["JsonContent"],
                    in: "header",
                    name: "Http-Rpc-Type",
                    required: true,
                    type: "string"
                }];

                let body = {argsTypes: argsTypes, argsObjs: argsObjs};
                req.parameters = {body: body, 'header.Http-Rpc-Type': "JsonContent"};
            }

            console.log(req)

            let parsedRequest = Object.assign({}, req)
            parsedRequest = fn.buildRequest(parsedRequest)

            console.log(parsedRequest)

            specActions.setRequest(req.pathName, req.method, parsedRequest)

            let requestInterceptorWrapper = function (r) {
                let mutatedRequest = requestInterceptor.apply(this, [r])
                let parsedMutatedRequest = Object.assign({}, mutatedRequest)
                specActions.setMutatedRequest(req.pathName, req.method, parsedMutatedRequest)
                return mutatedRequest
            }

            req.requestInterceptor = requestInterceptorWrapper
            req.responseInterceptor = responseInterceptor

            // track duration of request
            const startTime = Date.now()

            return fn.execute(req)
                .then(res => {
                    res.duration = Date.now() - startTime;
                    specActions.setResponse(req.pathName, req.method, res)
                })
                .catch(
                    err => specActions.setResponse(req.pathName, req.method, {
                        error: true, err: err
                    })
                )
        };

    const hsfSupportPlugin = function (system) {
        return {
            statePlugins: {
                spec: {
                    actions: {
                        executeRequest: executeRequest
                    }
                }
            }
        }
    };

    const buildSystemAsync = async (baseUrl) => {
        try {
            const configUIResponse = await fetch(
                baseUrl + "/swagger-resources/configuration/ui",
                {
                    credentials: 'same-origin',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                });
            const configUI = await configUIResponse.json();

            const configSecurityResponse = await fetch(
                baseUrl + "/swagger-resources/configuration/security",
                {
                    credentials: 'same-origin',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                });
            const configSecurity = await configSecurityResponse.json();

            const resourcesResponse = await fetch(
                baseUrl + "/swagger-resources",
                {
                    credentials: 'same-origin',
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                });
            const resources = await resourcesResponse.json();
            resources.forEach(resource => {
                if (resource.url.substring(0, 4) !== 'http') {
                    resource.url = baseUrl + resource.url;
                }
            });

            window.ui = getUI(baseUrl, resources, configUI, configSecurity);
        } catch (e) {
            const retryURL = await prompt(
                "Unable to infer base url. This is common when using dynamic servlet registration or when" +
                " the API is behind an API Gateway. The base url is the root of where" +
                " all the swagger resources are served. For e.g. if the api is available at http://example.org/api/v2/api-docs" +
                " then the base url is http://example.org/api/. Please enter the location manually: ",
                window.location.href);

            return buildSystemAsync(retryURL);
        }
    };

    const getUI = (baseUrl, resources, configUI, configSecurity) => {
        const ui = SwaggerUIBundle({
            /*--------------------------------------------*\
             * Core
            \*--------------------------------------------*/
            configUrl: null,
            dom_id: "#swagger-ui",
            dom_node: null,
            spec: {},
            url: "",
            urls: resources,
            /*--------------------------------------------*\
             * Plugin system
            \*--------------------------------------------*/
            layout: "StandaloneLayout",
            plugins: [
                SwaggerUIBundle.plugins.DownloadUrl,
                hsfSupportPlugin
            ],
            presets: [
                SwaggerUIBundle.presets.apis,
                SwaggerUIStandalonePreset
            ],
            /*--------------------------------------------*\
             * Display
            \*--------------------------------------------*/
            deepLinking: configUI.deepLinking,
            displayOperationId: configUI.displayOperationId,
            defaultModelsExpandDepth: configUI.defaultModelsExpandDepth,
            defaultModelExpandDepth: configUI.defaultModelExpandDepth,
            defaultModelRendering: configUI.defaultModelRendering,
            displayRequestDuration: configUI.displayRequestDuration,
            docExpansion: configUI.docExpansion,
            filter: configUI.filter,
            maxDisplayedTags: configUI.maxDisplayedTags,
            operationsSorter: configUI.operationsSorter,
            showExtensions: configUI.showExtensions,
            tagSorter: configUI.tagSorter,
            /*--------------------------------------------*\
             * Network
            \*--------------------------------------------*/
            oauth2RedirectUrl: baseUrl + "/webjars/springfox-swagger-ui/oauth2-redirect.html",
            requestInterceptor: (a => a),
            responseInterceptor: (a => a),
            showMutatedRequest: true,
            supportedSubmitMethods: configUI.supportedSubmitMethods,
            validatorUrl: configUI.validatorUrl,
            /*--------------------------------------------*\
             * Macros
            \*--------------------------------------------*/
            modelPropertyMacro: null,
            parameterMacro: null,
        });

        ui.initOAuth({
            /*--------------------------------------------*\
             * OAuth
            \*--------------------------------------------*/
            clientId: configSecurity.clientId,
            clientSecret: configSecurity.clientSecret,
            realm: configSecurity.realm,
            appName: configSecurity.appName,
            scopeSeparator: configSecurity.scopeSeparator,
            additionalQueryStringParams: configSecurity.additionalQueryStringParams,
            useBasicAuthenticationWithAccessCodeGrant: configSecurity.useBasicAuthenticationWithAccessCodeGrant,
        });

        return ui;
    };

    const getBaseURL = () => {
        const urlMatches = /(.*)\/swagger-ui.html.*/.exec(window.location.href);
        return urlMatches[1];
    };

    /* Entry Point */
    (async () => {
        await buildSystemAsync(getBaseURL());
    })();
};