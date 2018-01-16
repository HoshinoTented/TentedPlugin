package tented.annotations

/**
 * NotProperty
 * 一个可爱的注解
 * 只能用于类属性中
 * 表示某个属性不被作为属性看待
 * //主要是为了description函数。。。
 * @author Hoshino Tented
 * @date 2018/1/17 4:38
 */

@Target(AnnotationTarget.FIELD)
annotation class NotProperty