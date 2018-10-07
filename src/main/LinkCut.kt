data class Node(
  var left: Node? = null,
  var right: Node? = null,
  var parent: Node? = null,
  var link: Node? = null,
  var reverted: Boolean = false
) {

  fun isLeftChild() = parent?.left === this

  fun isRightChild() = parent?.right === this

  //TODO: made recursive?
  fun revert() {
    //println("revert")
    if (!reverted) return
    val tmp = right
    right = left
    left = tmp
    reverted = false
    left?.apply { reverted = !reverted }
    right?.apply { reverted = !reverted }
  }

  fun rotateLeft() {
    val x = requireNotNull(right)
    val z = parent
    if (z != null) {
      if (isLeftChild()) z.left = x
      else z.right = x
    }
    right = x.left
    x.left = this
    x.parent = z
    parent = x

    right?.parent = this
  }

  fun rotateRight() {
    val x = requireNotNull(left)
    val z = parent
    if (z != null) {
      if (isLeftChild()) z.left = x
      else z.right = x
    }
    left = x.right
    x.right = this
    x.parent = z
    parent = x

    left?.parent = this
  }
}

fun splay(x: Node) {
  //
  var p: Node
  var pp: Node?
  while (true) {
    p = x.parent ?: break
    pp = p.parent
    pp?.revert()
    p.revert()
    x.revert()
    if (pp == null) {
      x.link = p.link
      p.link = null
    } else if (pp.parent == null) {
      x.link = pp.link
      pp.link = null
    }
    if (x.isLeftChild()) {
      if (pp == null) {
        p.rotateRight()
        return
      }
      if (p.isLeftChild()) { //sig-zig
        x.parent!!.parent!!.rotateRight()
        x.parent!!.rotateRight()
      } else { //zig-zag
        x.parent!!.rotateRight()
        x.parent!!.rotateLeft()
      }
    } else {
      when {
        pp == null -> {
          p.rotateLeft()
          return
        }
        p.isRightChild() -> { //zig-zig
          x.parent!!.parent!!.rotateLeft()
          x.parent!!.rotateLeft()
        }
        else -> { //zig-zag
          x.parent!!.rotateLeft()
          x.parent!!.rotateRight()
        }
      }
    }
  }
  x.revert()
}

fun expose(v: Node) {
  //
  splay(v)
  v.right?.apply {
    link = v
    parent = null
  }
  v.right = null
  while (v.link != null) {
    val w = checkNotNull(v.link)
    splay(w)

    w.right?.apply {
      link = w
      parent = null
    }
    w.right = v
    v.parent = w
    v.link = null

    splay(v)
  }
}

fun makeRoot(node: Node) {
  expose(node)
  node.reverted = !node.reverted
}

fun getRightMostNode(node: Node): Node {
  node.revert()
  var res = node
  while (res.right != null) {
    res = res.right!!
    res.revert()
  }
  return res
}

fun cut(v: Node, w: Node) {
  check(v != w)
  expose(v)
  if (v.left != null && getRightMostNode(v.left!!) === w) {
    v.left!!.parent = null
    v.left = null
  } else {
    splay(w)
    if (w.left == null && w.link == v) {
      w.link = null
    }
  }

}

fun link(v: Node, w: Node) {
  makeRoot(v)
  expose(w)
  if (v.parent != null || v.link != null) return
  w.right = v
  v.parent = w
}

fun isConnected(v: Node, w: Node): Boolean {
  if (v === w) return true
  makeRoot(v)
  expose(w)
  return v.parent != null || v.link != null
}

fun main(args: Array<String>) {
  val id2Node = mutableMapOf<Int, Node>()
  while (true) {
    val line = readLine() ?: break
    if (line == "exit") break
    var cmd: String
    var u: Int
    var v: Int
    try {
      cmd = line.split(' ')[0]
      u = line.split(' ')[1].toInt()
      v = line.split(' ')[2].toInt()
    } catch (e: Exception) {
      System.err.println("Bad command")
      continue
    }
    val nu = id2Node.getOrPut(u) { Node() }
    val nv = id2Node.getOrPut(v) { Node() }
    when (cmd) {
      "link" -> link(nu, nv)
      "cut" -> cut(nu, nv)
      "ask" -> println(isConnected(nu, nv))
      else -> System.err.println("Unknown command")
    }
  }
}