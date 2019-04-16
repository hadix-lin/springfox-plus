package io.github.hadixlin.springfoxplus

import io.github.hadixlin.springfoxplus.javadoc.DEFAULT_CLASSPATH_PARENT
import io.github.hadixlin.springfoxplus.javadoc.parse
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import java.io.File

/**
 * springfox-plus:javadoc
 */
@Mojo(name = "javadoc")
class JavaDocMojo : AbstractMojo() {

	@field:Parameter(defaultValue = "\${project.build.sourceDirectory}")
	private lateinit var sourceDir: File
	@field:Parameter(defaultValue = "\${project.build.outputDirectory}")
	private lateinit var targetDir: File
	@field:Parameter
	private var packages: Array<String> = arrayOf()

	override fun execute() {
		val log = log
		log.info("读取源文件javadoc并生成静态文档")
		log.info("targetDir=" + targetDir.path)
		log.info("sourceDir=" + sourceDir.path)
		val outDir = File(targetDir, DEFAULT_CLASSPATH_PARENT).path

		if (packages.isNotEmpty()) {
			for (p in packages) {
				val packagePath = p.replace("\\.".toRegex(), "/")
				val root = File(sourceDir, packagePath)
				log.debug("parse ${root.path}")
				parse(root, outDir)
			}
		} else {
			log.debug("parse ${sourceDir.path}")
			parse(sourceDir, outDir)
		}
		log.info("packages=$packages")
	}

}