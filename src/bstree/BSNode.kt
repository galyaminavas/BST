package bstree

import tree.Node

class BSNode<Key : Comparable<Key>, Value>(var key: Key, var value: Value): Node<Key, Value>() {
    var parent: BSNode<Key, Value>? = null
    var leftChild: BSNode<Key, Value>? = null
    var rightChild: BSNode<Key, Value>? = null
}