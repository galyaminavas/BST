package btree

class BNode<Key : Comparable<Key>> {
    var keys = ArrayList<Key>()
    var children = ArrayList<BNode<Key>>()

    fun isLeaf(): Boolean {
        return (children.size == 0)
    }

}