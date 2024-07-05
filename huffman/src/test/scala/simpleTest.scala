package huffman

class HuffmanCharTest extends munit.FunSuite:

  import HuffmanChar.*

  test("weight: weight of a simple leaf"):
    assertEquals(weight(Leaf('c', 29)), 29)
