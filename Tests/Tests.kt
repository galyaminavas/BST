import bstree.*
import btree.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.util.*

internal class StressTests {
    var btree = BTree<Int>(1000)
    var tree = BSTree<Int, Int>()

    companion object{
        var testingSet = mutableSetOf<Int>()

        @BeforeAll @JvmStatic
        fun onStart() {
            for (i in 1..10000000)
                testingSet.add(i)
            /*while (testingSet.size < 1000){
                testingSet.add(Random().nextInt())
            }*/
        }
    }

    /*@Test
    fun addBinarySearchTree() {
        for (i in testingSet)
        tree.add(i, i)
    }


    @Test
    fun findBinarySearchTree() {
        for (i in testingSet)
        tree.search(i)
    }*/

    @Test
    fun addBTree() {
        for (i in testingSet)
            btree.add(i)
    }

    @Test
    fun findBTree() {
        for (i in testingSet)
            btree.search(i)
    }
}