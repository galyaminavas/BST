package tree

interface Tree<Key: Comparable<Key>, Value>: Iterable<Node<Key, Value>> {

    fun add(key: Key, value: Value)

    fun search(key: Key): Node<Key, Value>?

    fun delete(key: Key)

}