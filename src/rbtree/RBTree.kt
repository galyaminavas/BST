package rbtree

import tree.*

class RBTree<Key : Comparable<Key>, Value>: Tree<Key, Value> {

    var root: RBNode<Key, Value>? = null

    internal var nodesCount: Int = 0

    private fun rotateLeft(node: RBNode<Key, Value>) {
        var rightson: RBNode<Key, Value> = node.rightChild!! //right son
        node.rightChild = rightson.leftChild
        rightson.leftChild?.parent = node //son's left subtree

        if (node.parent == null) //if node is a root
            root = rightson
        rightson.parent = node.parent

        rightson.leftChild = node //son's new left subtree
        node.parent = rightson

        if (rightson.parent?.leftChild == node)
            rightson.parent?.leftChild = rightson
        else
            rightson.parent?.rightChild = rightson
    }

    private fun rotateRight(node: RBNode<Key, Value>) {
        var leftson: RBNode<Key, Value> = node.leftChild!!
        node.leftChild = leftson.rightChild
        leftson.rightChild?.parent = node

        if (node.parent == null)
            root = leftson
        leftson.parent = node.parent

        leftson.rightChild = node
        node.parent = leftson

        if (leftson.parent?.leftChild == node)
            leftson.parent?.leftChild = leftson
        else
            leftson.parent?.rightChild = leftson
    }

    private fun addFixup(node: RBNode<Key, Value>) {
        if (node == root) {
            node.colour = RBNode.Colour.Black
            return
        }

        if (node.parent?.colour == RBNode.Colour.Black)
            return

        if (node.uncle()?.colour == RBNode.Colour.Red) {
            //parent & uncle are red -> change colours
            node.parent!!.colour = RBNode.Colour.Black
            node.uncle()?.colour = RBNode.Colour.Black
            node.grandparent()?.colour = RBNode.Colour.Red
            addFixup(node.grandparent()!!)
        }
        else {
            //father is red, uncle is black
            if (node.grandparent()?.leftChild == node.parent) {
                //if parent of new node - left child
                if (node.parent!!.leftChild == node) {
                    //if new node is a left child
                    node.parent!!.colour = RBNode.Colour.Black
                    node.parent!!.parent?.colour = RBNode.Colour.Red
                    rotateRight(node.parent!!.parent!!)
                }
                else {
                    //if new node is a right child
                    rotateLeft(node.parent!!)
                    node.colour = RBNode.Colour.Black
                    node.parent?.colour = RBNode.Colour.Red
                    rotateRight(node.parent!!)
                }
            }
            else {
                //if parent of new node - right child
                if (node.parent!!.rightChild == node) {
                    //if new node is a right child
                    node.parent!!.colour = RBNode.Colour.Black
                    node.parent!!.parent?.colour = RBNode.Colour.Red
                    rotateLeft(node.parent!!.parent!!)
                }
                else {
                    //if new node is a left child
                    rotateRight(node.parent!!)
                    node.colour = RBNode.Colour.Black
                    node.parent?.colour = RBNode.Colour.Red
                    rotateLeft(node.parent!!)
                }
            }
        }
    }

    override fun search(key: Key): RBNode<Key, Value>? {
        var current: RBNode<Key, Value>? = root
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

    override fun add(key: Key, value: Value) {
        //if node with this key already exists - do not add new one
        if (search(key) != null)
            return
        var current: RBNode<Key, Value>? = root
        val newRBNode = RBNode(key, value)
        newRBNode.colour = RBNode.Colour.Red
        if (root == null) {
            root = newRBNode
            nodesCount++
            return addFixup(newRBNode)
        }
        while (current != null) {
            if (key > current.key) {
                if (current.rightChild == null) {
                    current.rightChild = newRBNode
                    newRBNode.parent = current
                    nodesCount++
                    return addFixup(newRBNode)
                }
                current = current.rightChild
            } else {
                if (current.leftChild == null) {
                    current.leftChild = newRBNode
                    newRBNode.parent = current
                    nodesCount++
                    return addFixup(newRBNode)
                }
                current = current.leftChild
            }
        }
    }

    private fun min(x: RBNode<Key, Value>): RBNode<Key, Value> {
        if (x.leftChild == null)
            return x
        return min(x.leftChild!!)
    }

    private fun transplant(x: RBNode<Key, Value>, y: RBNode<Key, Value>?) {
        if (x == this.root)
            this.root = y
        else if (x == x.parent?.leftChild)
            x.parent?.leftChild = y
        else
            x.parent?.rightChild = y
        y?.parent = x.parent
    }

    private fun deleteFixup(x: RBNode<Key, Value>?) {
        var currNode = x
        while (currNode != this.root && currNode?.colour == RBNode.Colour.Black) {
            if (currNode == currNode.parent?.leftChild || currNode.parent?.leftChild == null) {
                var brother = currNode.parent?.rightChild
                if (brother?.colour == RBNode.Colour.Red) {
                    brother.colour = RBNode.Colour.Black
                    currNode.parent?.colour = RBNode.Colour.Red
                    rotateLeft(currNode.parent!!)
                    brother = currNode.parent?.rightChild
                }
                if ((brother?.leftChild?.colour == RBNode.Colour.Black || brother?.leftChild == null)
                        && (brother?.rightChild?.colour == RBNode.Colour.Black || brother?.rightChild == null)) {
                    brother?.colour = RBNode.Colour.Red
                    currNode = currNode.parent!!
                }
                else {
                    if (brother.rightChild?.colour == RBNode.Colour.Black || brother.rightChild == null) {
                        brother.leftChild?.colour = RBNode.Colour.Black
                        brother.colour = RBNode.Colour.Red
                        rotateRight(brother)
                        brother = currNode.parent?.rightChild
                    }
                    brother?.colour = currNode.parent!!.colour
                    currNode.parent?.colour = RBNode.Colour.Black
                    brother?.rightChild?.colour = RBNode.Colour.Black
                    rotateLeft(currNode.parent!!)
                    currNode = this.root!!
                }
            }
            else {
                var brother = currNode.parent?.leftChild
                if (brother?.colour == RBNode.Colour.Red) {
                    brother.colour = RBNode.Colour.Black
                    currNode.parent?.colour = RBNode.Colour.Red
                    rotateRight(currNode.parent!!)
                    brother = currNode.parent?.leftChild
                }
                if ((brother?.leftChild?.colour == RBNode.Colour.Black || brother?.leftChild == null)
                        && (brother?.rightChild?.colour == RBNode.Colour.Black || brother?.rightChild == null)) {
                    brother?.colour = RBNode.Colour.Red
                    currNode = currNode.parent!!
                }
                else {
                    if (brother.leftChild?.colour == RBNode.Colour.Black || brother.leftChild == null) {
                        brother.rightChild?.colour = RBNode.Colour.Black
                        brother.colour = RBNode.Colour.Red
                        rotateLeft(brother)
                        brother = currNode.parent?.leftChild
                    }
                    brother?.colour = currNode.parent!!.colour
                    currNode.parent?.colour = RBNode.Colour.Black
                    brother?.leftChild?.colour = RBNode.Colour.Black
                    rotateRight(currNode.parent!!)
                    currNode = this.root!!
                }
            }
        }
        currNode?.colour = RBNode.Colour.Black
    }

    override fun delete(key: Key) {
        var delNode = search(key)
        if (delNode == null)
            return

        if (delNode == this.root) {
            this.root = null
            nodesCount--
            return
        }

        var succNode = delNode
        var x : RBNode<Key, Value>?
        var delColour = succNode.colour
        if (delNode.leftChild == null) {
            x = delNode.rightChild
            transplant(delNode, delNode.rightChild)
        }
        else if (delNode.rightChild == null) {
                x = delNode.leftChild
                transplant(delNode, delNode.leftChild)
        }
        else {
            succNode = min(delNode.rightChild!!)
            delColour = succNode.colour
            x = succNode.rightChild
            if (succNode.parent == delNode) {
                x?.parent = succNode
            }
            else {
                transplant(succNode, succNode.rightChild)
                succNode.rightChild = delNode.rightChild
                succNode.rightChild?.parent = succNode
            }
            transplant(delNode, succNode)
            succNode.leftChild = delNode.leftChild
            succNode.leftChild?.parent = succNode
            succNode.colour = delNode.colour
        }

        if (delColour == RBNode.Colour.Black)
            if (x != null)
                deleteFixup(x)
            else
                deleteFixup(succNode)
        nodesCount--
    }

    internal fun height(node: RBNode<Key, Value>?): Int {
        if (node == null)
            return 0
        else {
            var leftHeight = height(node.leftChild)
            var rightHeight = height(node.rightChild)
            if (leftHeight > rightHeight)
                return leftHeight + 1
            else
                return rightHeight + 1
        }
    }

    internal fun printLevel(root: RBNode<Key, Value>?, level: Int) {
        if (root == null)
            return
        if (level == 1) {
            if (root.colour == RBNode.Colour.Black)
                print("${root.value} ")
            else
                print("[${root.value}] ")
        }
        else {
                printLevel(root.leftChild, level - 1)
                printLevel(root.rightChild, level - 1)
        }
    }

    internal fun printLevelOrderTraversal() {
        val h = height(root)
        for (i in 1..h) {
            printLevel(root, i)
            println()
        }
    }

    private fun first(): RBNode<Key, Value>? {
        var currNode = this.root
        while (currNode?.leftChild != null)
            currNode = currNode.leftChild
        return currNode
    }

    private fun nextElement(node: RBNode<Key, Value>?): RBNode<Key, Value>? {
        var currNode: RBNode<Key, Value>? = node ?: return null
        if (currNode?.rightChild != null) {
            currNode = currNode.rightChild
            while (currNode?.leftChild != null)
                currNode = currNode.leftChild
        }
        else {
            while (currNode != null && currNode.key <= node.key)
                currNode = currNode.parent
        }
        return currNode
    }

    override fun iterator(): Iterator<RBNode<Key, Value>> = object: Iterator<RBNode<Key, Value>> {
        var node = first()

        override fun next(): RBNode<Key, Value> {
            val result = node
            node = nextElement(node)
            return result!!
        }

        override fun hasNext(): Boolean {
            return node != null
        }
    }

    fun printElements() {
        for (it in this)
            print("${it.value} ")
    }
}