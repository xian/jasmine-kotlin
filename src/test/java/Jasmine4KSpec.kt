package com.squareup.jasmine4k

import java.util.List
import java.util.ArrayList
import com.squareup.Expect
import com.squareup.ZExpect

class ThingTests {
  fun testForTestEnvSetup() {
    describe("MyClass") {
      it("should have behavior A") {
      }

      context("other case") {
        it("should have behavior B") {
        }
      }
    }
  }

  fun testExpectationFailures() {
    describe("MyClass") {
      it("should have behavior A") {
//        Expect.expect("abc").toEqual("def")
//        expect("abc").toEqual("def")
//        expect("abc").not.toEqual("def")
//        expect(true).toBeFalse()
      }

      context("other case") {
        it("should have behavior B") {
        }
      }
    }
  }

  fun testBeforesAndAfters(log:List<String>) {
    describe("MyClass") {
      before { log add "outer before" }
      after { log add "outer after" }

      it("first it") {
        log add "in first it"
      }

      context("context") {
        before { log add "context before 1" }
        before { log add "context before 2" }
        after { log add "context after 1" }
        after { log add "context after 2" }

        it("second it") {
          log add "in inner it"
        }
      }

      it("third it") {
        log add "in third it"
      }
    }
  }

  fun testLets() {
    describe("MyClass") {
      var user = let { "nobody" }
      subject { "foo" }

      context("logged in") {
        user = let { "somebody" }

        it("displays your name") {
//          val objectMatcher = expect(1)
          println("subject(): ${user()}")
        }
      }

      it("should still be logged out") {
        println("user should be nobody: ${user()}")
      }
    }
  }
}
