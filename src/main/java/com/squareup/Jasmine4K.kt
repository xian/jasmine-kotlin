package com.squareup.jasmine4k

import std.io.*
import java.util.List
import java.util.ArrayList

class Env {
  class object {
    public var currentEnv:Env? = null
    var currentSuite:Suite? = null
  }

  public val childSpecs:List<Spec> = ArrayList()


  public fun before(body:()->Unit) {
    currentSuite?.befores?.add(body)
  }

  public fun after(body:()->Unit) {
    currentSuite?.afters?.add(body)
  }

  public fun describe(val name:String, val body:()->Unit) {
    val suite = Suite(name, body, currentSuite)
    addAsChild(suite)
    val prevSuite = Env.currentSuite
    Env.currentSuite = suite
    suite.declare()
    Env.currentSuite = prevSuite
  }

  public fun context(val name:String, val body:()->Unit) {
    describe(name, body)
  }

  public fun it(val name:String, val body:()->Unit) {
    val spec = Spec(name, body, currentSuite)
    addAsChild(spec)
  }

  public fun execute() {
    for (childSpec in childSpecs) {
      childSpec.execute()
    }
  }

  private fun addAsChild(spec:Spec) {
    if (currentSuite == null) {
      childSpecs.add(spec)
    } else {
      currentSuite?.childSpecs?.add(spec)
    }
  }
}

open class Spec(val name:String, val body:()->Unit, val parent:Suite? = null) {
  public val befores:List<()->Unit> = ArrayList()
  public val afters:List<()->Unit> = ArrayList()
  public val childSpecs:List<Spec> = ArrayList()

  open fun execute() {
    runBefores()
    (body)()
    runAfters()
  }

  fun runBefores() {
    parent?.runBefores()
    for (before in befores) {
      before()
    }
  }

  fun runAfters() {
    for (after in afters) {
      after()
    }
    parent?.runAfters()
  }
}

class Suite(name:String, body:()->Unit, parent:Suite? = null):Spec(name, body, parent) {
  fun declare() {
    val x = this.body
    x()
  }

  override fun execute() {
    for (childSpec in childSpecs) {
      childSpec.execute()
    }
  }
}

fun before(body:()->Unit) {
  Env.currentEnv?.before(body)
}

fun after(body:()->Unit) {
  Env.currentEnv?.after(body)
}

fun describe(name:String, body:()->Unit) {
  println("Describing ${name}...")
  Env.currentEnv?.describe(name, body)
}

fun context(name:String, body:()->Unit) {
  println("Context ${name}...")
  Env.currentEnv?.context(name, body)
}

fun it(name:String, body:()->Unit) {
  println("It ${name}.")
  Env.currentEnv?.it(name, body)
}

fun <T> subject():T? {
  return null
}

fun subject(body:()->Unit) {
  body()
}

fun <T> let(body:()->T):()->T {
  return body
}
