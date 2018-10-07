import kotlin.random.Random

class Forest(size: Int) {
  val connected = Array(size) { BooleanArray(size) { false } }.apply {
    (0 until size).forEach { this[it][it] = true }
  }
  val roots = (0 until size).toMutableList()
  fun childrenOf(root: Int): Set<Int> {
    check(root in roots)
    val visited = mutableSetOf<Int>()
    val result = mutableSetOf<Int>()
    fun dfs(from: Int) {
      visited += from
      if (connected[from].filter { it }.size == 1 && connected[from][from]) {
        result += from
        return
      }
      connected[from].withIndex().filter { (i, v) -> v && i !in visited }.forEach {
        dfs(it.index)
      }
    }
    dfs(root)
    return result
  }
  fun generateEdge(): Pair<Int, Int> {
    check(roots.size > 1) { "Only one tree in the forest" }
    val rand = Random(42)
    val first = rand.nextInt(roots.size)
    var second = rand.nextInt(roots.size)
    if (first == second) {
      if (second < roots.size-1) second += 1 else second -= 1
    }
    val firstRootIdx = roots[first]
    val children = childrenOf(firstRootIdx).toList()
    val child = children[rand.nextInt(children.size)]
    return child to roots[second]
  }
  fun isConnected(u: Int, v: Int): Boolean {
    val visited = mutableSetOf<Int>()
    fun dfs(from: Int): Boolean {
      if (from == v) return true
      visited += from
      connected[from].withIndex().filter { (i, v) -> v && i !in visited }.forEach {
        if (dfs(it.index)) return true
      }
      return false
    }
    return dfs(u)
  }
  //fun removeEdge()
}