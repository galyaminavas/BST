class RBNode<Key : Comparable<Key>, Value>(var key: Key, var value: Value) {
    enum class Colour {Black, Red}

    var parent: RBNode<Key, Value>? = null
    var leftChild: RBNode<Key, Value>? = null
    var rightChild: RBNode<Key, Value>? = null
    var colour: Colour = Colour.Black

    fun grandparent(): RBNode<Key, Value>? {
        if (this != null && this.parent != null) {
            val p: RBNode<Key, Value>? = this.parent
            return p?.parent
        }
        else
            return null
    }

    fun uncle(): RBNode<Key, Value>? {
        val g: RBNode<Key, Value>? = this.grandparent() ?: return null
        if (this.parent == g?.leftChild)
            return g?.rightChild
        else
            return g?.leftChild
    }
}