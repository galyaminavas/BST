package rbtree

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class RBTreeTest {
    val testTree = RBTree<Int, Int>()

    @Test
    fun rootColour() {
        testTree.add(13, 13)
        assertEquals(testTree.root?.colour, RBNode.Colour.Black)
        for (i in 50 downTo 1) {
            testTree.add(i, i)
            assertEquals(testTree.root?.colour, RBNode.Colour.Black)
        }
    }

    @Test
    fun nodesNumber() {
        assertEquals(testTree.nodesCount, 0)
        testTree.add(13, 13)
        assertEquals(testTree.nodesCount, 1)
        for (i in 1..50) {
            testTree.add(i, i)
        }
        assertEquals(testTree.nodesCount, 50)
    }

    @Test
    fun deleteExistingNode() {
        for (i in 1..50)
            testTree.add(i, i)
        testTree.delete(14)
        assertEquals(testTree.nodesCount, 49)
    }

    @Test
    fun deleteNonExistingNode() {
        for (i in 1..50)
            testTree.add(i, i)
        testTree.delete(60)
        assertEquals(testTree.nodesCount, 50)
    }

    @Test
    fun doubleDeletion() {
        for (i in 1..50)
            testTree.add(i, i)
        var j = 1
        for (i in 41..50) {
            testTree.delete(i)
            assertEquals(testTree.nodesCount, 50 - j)
            j++
        }
        for (i in 41..50) {
            testTree.delete(i)
            assertEquals(testTree.nodesCount, 40)
        }
    }

    @Test
    fun rootDeletion() {
        testTree.add(13, 13)
        testTree.delete(13)
        assertNull(testTree.root)
        assertEquals(testTree.nodesCount, 0)
    }

    @Test
    fun findInEmptyTree() {
        assertNull(testTree.search(13))
    }

    @Test
    fun findExistingNode() {
        testTree.add(13, 13)
        for (i in 1..50)
            testTree.add(i, i)
        assertEquals(testTree.search(48)?.value, 48)
    }

    @Test
    fun findNonExistingNode() {
        for (i in 1..50)
            testTree.add(i, i)
        assertNull(testTree.search(69))
    }

    @Test
    fun iteratorForEmptyTree() {
        for (i in testTree)
            //condition is never true
            assertEquals(0, 1)
    }

    @Test
    fun iteratorTest() {
        for (i in 1..50)
            testTree.add(i, i)
        var i = 0
        for (node in testTree) {
            i++
            assertEquals(node.value, i)
        }
        assertEquals(testTree.nodesCount, i)
    }
}
