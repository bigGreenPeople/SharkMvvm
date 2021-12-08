/**
 * 移动元素到第一
 * @receiver MutableList<E>
 * @param element E
 */
fun <E> MutableList<E>.moveFirst(element: E) {
    this.move(0, element)
}

/**
 * 移动元素到最后
 * @receiver MutableList<E>
 * @param element E
 */
fun <E> MutableList<E>.moveLast(element: E) {
    this.remove(element)
    this.add(size, element)
}

/**
 * 移动元素
 * @receiver MutableList<E>
 * @param index Int
 * @param element E
 */
fun <E> MutableList<E>.move(index: Int, element: E) {
    this.remove(element)
    this.add(index, element)
}