package co.fanavari.myapplication.huawei

interface LogNode {
    fun println(priority: Int, tag: String?, msg: String?, tr: Throwable?)
}