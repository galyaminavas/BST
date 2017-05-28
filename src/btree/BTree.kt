package btree

import java.util.*

class BTree<Key : Comparable<Key>>(val t: Int) {
    var root: BNode<Key>? = BNode()

    fun add(key: Key) {
        //if node with this key already exists - do not add new one
        if (this.search(key) != null)
            return
        if (root?.keys?.size == 2 * t - 1) {
            var newNode = BNode<Key>()
            newNode.children.add(root!!)
            splitChild(newNode, 0, root!!)
            this.root = newNode
        }
        addNonFull(root!!, key)
    }

    fun addNonFull(node: BNode<Key>, key: Key) {
        var i = node.keys.size - 1
        while (i >= 0 && key < node.keys[i])
            i--
        i++
        if (node.isLeaf()) {
            node.keys.add(i, key)
        }
        else {
            if (node.children[i].keys.size == 2 * t - 1) {
                splitChild(node, i, node.children[i])
                if (key > node.keys[i])
                    i++
            }
            addNonFull(node.children[i], key)
        }
    }

    fun search(key: Key): Key? {
        var subroot: BNode<Key>? = root
        if (subroot == null)
            return null
        var i = 0
        while (subroot != null) {
            while (i < subroot.keys.size && key > subroot.keys[i])
                i++
            if (i < subroot.keys.size && key == subroot.keys[i])
                return subroot.keys[i]
            if (subroot.isLeaf())
                return null
                //subroot = null
            else
                subroot = subroot.children[i]
        }
        return null
    }

    fun splitChild(node: BNode<Key>, i: Int, child: BNode<Key>) {
        var newNode = BNode<Key>()

        for (j in 0..t - 2) {
            newNode.keys.add(child.keys[t])
            child.keys.removeAt(t)
        }

        if (!child.isLeaf()) {
            for (j in 0..t - 1) {
                newNode.children.add(child.children[t])
                child.children.removeAt(t)
            }
        }

        node.children.add(i + 1, newNode)
        node.keys.add(i, child.keys[t - 1])
        child.keys.removeAt(t - 1)
    }

    fun print() {
        var list: Queue<BNode<Key>> = LinkedList()
        var listChar: Queue<Char> = LinkedList()

        list.add(root)
        listChar.add('\n')

        var specSymbol = '\n'

        while (!list.isEmpty()) {
            val currNode = list.poll()

            for (key in currNode.keys)
                print(" $key ")

            if (!currNode.isLeaf()) {
                for (child in currNode.children) {
                    list.add(child)
                    listChar.add('|')
                }
            }
            if (listChar.peek() == '\n') {
                listChar.add(specSymbol)
                print(listChar.poll())
            }
            print(listChar.poll())
        }

    }

    /*fun findKeyInNode(node: BNode<Key>, key: Key): Int {
        var ind = 0
        while (ind < node.keys.size && node.keys[ind] < key)
            ind++
        return ind
    }

    fun removeFromLeaf(node: BNode<Key>, ind: Int) {
        node.keys.removeAt(ind)
        return
    }

    fun getPred(node: BNode<Key>, ind: Int): Key {
        //move to the right most leaf until we reach a leaf
        var curr = node.children[ind]
        while (!curr.isLeaf()) {
            curr = curr.children[curr.keys.size]
        }
        //return the last key of the leaf
        return curr.keys[curr.keys.size - 1]
    }

    fun getSucc(node: BNode<Key>, ind: Int): Key {
        //move to the left most node until reach a leaf
        var curr = node.children[ind + 1]
        while (!curr.isLeaf()) {
            curr = curr.children[0]
        }
        //return the first key of the leaf
        return curr.keys[0]
    }
    // A function to merge C[ind] with C[ind+1]
    fun merge(node: BNode<Key>, ind: Int) {
        var child = node.children[ind]
        var sibling = node.children[ind + 1]
        // Pulling a key from the current node and inserting it into (t-1)th
        // position of C[ind]
        child.keys[t - 1] = node.keys[ind]
        // Copying the keys from C[ind+1] to C[ind] at the end
        for (i in 0..sibling.keys.size - 1)
            child.keys[i + t] = sibling.keys[i]
        // Copying the child pointers from C[ind+1] to C[ind]
        if (!child.isLeaf())
            for (i in 0..sibling.keys.size)
                child.children[i + t] = sibling.children[i]
        // Moving all keys after ind in the current node one step before -
        // to fill the gap created by moving keys[ind] to C[ind]
        for (i in ind + 1..node.keys.size - 1)
            node.keys[i - 1] = node.keys[i]
        // Moving the child pointers after (ind+1) in the current node one
        // step before
        for (i in ind + 2..node.keys.size)
            node.children[i - 1] = node.children[i]

        //delete sibling
        return
    }

    fun removeFromNonLeaf(node: BNode<Key>, ind: Int) {
        var key = node.keys[ind]

        //if the child that precedes key has at least t keys
        //find the predecessor of key in the subtree of children[ind]
        //replace key by pred
        //recursively delete pred in children[ind]

        if (node.children[ind].keys.size >= t) {
            var pred = getPred(node, ind)
            node.keys[ind] = pred
            recursiveRemove(node.children[ind], pred)
        }
        //if the child [ind] has less then t keys, check [ind + 1]
        //if [ind + 1] has at least t keys, find the succ of key in the subtree of [ind + 1]
        //replace key by succ
        //recursively delete succ in [ind + 1]
        else if (node.children[ind + 1].keys.size >= t) {
            var succ = getSucc(node, ind)
            node.keys[ind] = succ
            recursiveRemove(node.children[ind + 1], succ)
        }
        //if both [ind] and [ind + 1] has less that t keys, merge key and
        //all [ind + 1] into [ind]; [ind] contains 2t-1 keys
        //recursively delete key from [ind]
        else {
            merge(node, ind)
            recursiveRemove(node.children[ind], key)
        }
        return
    }

    fun borrowFromPrev(node: BNode<Key>, ind: Int) {
        var child = node.children[ind]
        var sibling = node.children[ind - 1]

        // The last key from C[ind-1] goes up to the parent and key[ind-1]
        // from parent is inserted as the first key in C[ind]. Thus, the  loses
        // sibling one key and child gains one key

        // Moving all key in C[ind] one step ahead
        for (i in child.keys.size - 1 downTo 0)
            child.keys[i + 1] = child.keys[i]
        // If C[ind] is not a leaf, move all its child pointers one step ahead
        if (!child.isLeaf())
            for (i in child.keys.size downTo 0)
                child.children[i + 1] = child.children[i]

        // Setting child's first key equal to keys[ind-1] from the current node
        child.keys[0] = node.keys[ind - 1]

        // Moving sibling's last child as C[ind]'s first child
        if (!node.isLeaf())
            child.children[0] = sibling.children[sibling.keys.size]

        // Moving the key from the sibling to the parent
        // This reduces the number of keys in the sibling
        node.keys[ind - 1] = sibling.keys[sibling.keys.size - 1]

        return
    }

    // A function to borrow a key from the C[ind+1] and place it in C[ind]
    fun borrowFromNext(node: BNode<Key>, ind: Int)
    {
        var child = node.children[ind]
        var sibling = node.children[ind + 1]

        // keys[ind] is inserted as the last key in C[ind]
        child.keys[(child.keys.size)] = node.keys[ind]

        // Sibling's first child is inserted as the last child
        // into C[ind]
        if (!child.isLeaf())
        child.children[child.keys.size + 1] = sibling.children[0]

        //The first key from sibling is inserted into keys[ind]
        node.keys[ind] = sibling.keys[0]

        // Moving all keys in sibling one step behind
        for (i in 1..sibling.keys.size)
            sibling.keys[i-1] = sibling.keys[i]

        // Moving the child pointers one step behind
        if (!sibling.isLeaf())
        {
            for(i in 1..sibling.keys.size)
                sibling.children[i - 1] = sibling.children[i]
        }

        return
    }

    // A function to fill child C[ind] which has less than t-1 keys
    fun fill(node: BNode<Key>, ind: Int) {
        // If the previous child(C[ind-1]) has more than t-1 keys, borrow a key
        // from that child
        if (ind != 0 && node.children[ind - 1].keys.size >= t)
            borrowFromPrev(node, ind)

        // If the next child(C[ind+1]) has more than t-1 keys, borrow a key
        // from that child
        else if (ind != node.keys.size && node.children[ind + 1].keys.size >= t)
            borrowFromNext(node, ind)

        // Merge C[ind] with its sibling
        // If C[ind] is the last child, merge it with with its previous sibling
        // Otherwise merge it with its next sibling
        else {
            if (ind != node.keys.size)
                merge(node, ind)
            else
                merge(node, ind - 1)
        }
        return
    }

    fun recursiveRemove(node: BNode<Key>, key: Key) {
        val ind = findKeyInNode(node, key)

        //the key to be removed is present in the node
        if (ind < node.keys.size && node.keys[ind] == key) {
            if (node.isLeaf())
                removeFromLeaf(node, ind)
            else
                removeFromNonLeaf(node, ind)
        }
        else {
            //if the node is a leaf, then the key does not present in the tree
            if (node.isLeaf())
                return

            //the key to be removed is present in the sub-tree of this node
            //the flag indicates whether the key is present in the sub-tree of the last child of this node
            val flag = if(ind == node.keys.size) 0 else 1

            //if the child where the key is supposed to be has less then t keys we will fill it
            if (node.children[ind].keys.size < t)
                fill(node, ind)

            //if the last child has been merged, it must have merged with the previous child
            //so we recurse on the (ind-1)th child
            if (flag == 1 && ind > node.keys.size)
                recursiveRemove(node.children[ind - 1], key)
            //else we recurse on the (ind)th child which has at least t keys
            else
                recursiveRemove(node.children[ind], key)
        }
        return
    }

    fun delete(key: Key) {
        if (root == null)
            return
        recursiveRemove(root!!, key)

        if (root?.keys?.size == 0) {
            if (root!!.isLeaf())
                root = null
            else
                root = root!!.children[0]
        }
    }*/
}