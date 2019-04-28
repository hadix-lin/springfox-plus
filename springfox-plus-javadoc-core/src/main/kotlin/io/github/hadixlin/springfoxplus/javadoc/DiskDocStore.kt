package io.github.hadixlin.springfoxplus.javadoc

import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption.READ

class DiskDocStore(storeDir: String) : JsonDocStore({ it.input(storeDir) }, { it.output(storeDir) })

private fun String.input(storeDir: String): InputStream {
    val path = this.typeToPath(storeDir)
    return Files.newInputStream(path, READ)
}

private fun String.typeToPath(parent: String): Path {
    val typePath = this.replace(Regex("\\."), "/")
    return Paths.get(parent, typePath)
}

private fun String.output(storeDir: String): OutputStream {
    val path = this.typeToPath(storeDir)
    Files.deleteIfExists(path)
    Files.createDirectories(path.parent)
    Files.createFile(path)
    return Files.newOutputStream(path)
}

