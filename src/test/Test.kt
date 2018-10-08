import org.junit.Test

class LCTest {
  @Test
  fun test1() {
    val vvv = Array(10) { Node() }
    check(!isConnected(vvv[0], vvv[1]))
    link(vvv[0], vvv[1])
    check(isConnected(vvv[0], vvv[1]))
    link(vvv[2], vvv[3])
    check(!isConnected(vvv[0], vvv[2]))
    link(vvv[0], vvv[3])
    check(isConnected(vvv[0], vvv[2]))
    check(isConnected(vvv[0], vvv[3]))
    check(isConnected(vvv[1], vvv[3]))
    check(isConnected(vvv[2], vvv[3]))
    link(vvv[4], vvv[3])
    check(isConnected(vvv[1], vvv[4]))
    link(vvv[4], vvv[5])
    check(isConnected(vvv[1], vvv[5]))
    cut(vvv[3], vvv[4])
    check(!isConnected(vvv[1], vvv[4]))
    check(!isConnected(vvv[1], vvv[5]))
    check(isConnected(vvv[4], vvv[5]))
    cut(vvv[5], vvv[4])
    check(!isConnected(vvv[4], vvv[5]))
  }

  @Test
  fun test2() {
    val v = Array(10) { Node() }
    link(v[0], v[1])
    link(v[2], v[3])
    link(v[2], v[4])
    check(isConnected(v[3], v[4]))
    check(isConnected(v[2], v[4]))
    check(!isConnected(v[0], v[4]))
    link(v[0], v[2])
    check(isConnected(v[1], v[2]))
    check(isConnected(v[0], v[4]))
    check(isConnected(v[3], v[0]))
    cut(v[2], v[0])
    check(!isConnected(v[1], v[2]))
    check(!isConnected(v[0], v[4]))
    check(!isConnected(v[3], v[0]))
  }
}