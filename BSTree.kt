package tree.bstree

import tree.*

class BSTree<Key : Comparable<Key>, Value>: Tree<Key, Value> {
    var root: BSNode<Key, Value>? = null

    override fun add(key: Key, value: Value) {
        //if node with this key already exists - do not add new one
        if (search(key) != null)
            return
        var current: BSNode<Key, Value>? = root
        val newNode = BSNode(key, value)
        if (root == null) {
            root = newNode
            return
        }
        while (current != null) {
            if (key > current.key) {
                if (current.rightChild == null) {
                    current.rightChild = newNode
                    newNode.parent = current
                    return
                }
                current = current.rightChild
            }
            else {
                if (current.leftChild == null) {
                    current.leftChild = newNode
                    newNode.parent = current
                    return
                }
                current = current.leftChild
            }
        }
    }
    
    override fun search(key: Key): BSNode<Key, Value>? {
        var current: BSNode<Key, Value>? = root
        if ((current?.key == key) || (current == null)) {
            return current
        }
        while (current != null) {
            if (key == current.key)
                return current
            if (key > current.key) {
                current = current.rightChild
            } else {
                current = current.leftChild
            }
        }
        return null
    }

    private fun min(x: BSNode<Key, Value>): BSNode<Key, Value> {
        if (x.leftChild == null)
            return x
        return min(x.leftChild!!)
    }

    private fun recursiveRemove(subroot: BSNode<Key, Value>, key: Key) {
        if (key < subroot.key)
            recursiveRemove(subroot.leftChild!!, key)
        else if (key > subroot.key)
            recursiveRemove(subroot.rightChild!!, key)
        else if (subroot.leftChild != null && subroot.rightChild != null) {
            subroot.key = min(subroot.rightChild!!).key
            subroot.value = min(subroot.rightChild!!).value
        }
        else if (subroot.leftChild != null) {
            subroot.key = subroot.leftChild!!.key
            subroot.value = subroot.leftChild!!.value
        }
        else {
            subroot.key = subroot.rightChild!!.key
            subroot.value = subroot.rightChild!!.value
        }
    }

    override fun delete(key: Key) {
        //if there is no such key
        if (search(key) == null)
            return
        recursiveRemove(root!!, key)
    }
}