package speedith.core.reasoning

import speedith.core.reasoning.automatic.wrappers.SpiderDiagramOccurrence

/**
 * Type definitions for tactic/tactical support. Tactical combinators
  * for tactics.
 *
 * @author Sven Linker [s.linker@brighton.ac.uk]
 *
 */
package object tactical {

  /**
    * Type for tactics. The parameters:
    * String: Name of the general tactic that is applied (for UI)
    * Goals: Current subgoals
    * Int: Index of target subgoal
    * TacticApplicationResult: Prior rule application from tactics
    */
  type Tactic           = String => Goals => Int => TacticApplicationResult => Option[TacticApplicationResult]
  type Chooser[A]       = SpiderDiagramOccurrence => Option[A]
  type DiagramPredicate = SpiderDiagramOccurrence => Boolean
  type GoalPredicate    = Goals => Int => Boolean


  /*
    Tacticals used to combine tactics.
   */

  /**
    * THEN takes two tactics as parameters. It executes the first and afterwards
    * executes the second on the result of the first. This combinator fails
    * if any of the two parameters fail.
    * @return
    */
  def THEN: Tactic => Tactic => Tactic = (tac1: Tactic) => (tac2: Tactic) => (name: String) => (state: Goals) => (subgoalIndex: Int) => (result: TacticApplicationResult) => {
    tac1(name)(state)(subgoalIndex)(result) flatMap (res => tac2(name)(res.getGoals)(subgoalIndex)(res))
  }

  /**
    * Executes the tactic it gets as a first parameter and returns its result if it succeeds.
    * If the tactic fails, it executes the second tactic. This combinator fails if both
    * parameters fail on the given state.
    * @return
    */
  def ORELSE: Tactic => Tactic => Tactic =
    (tac1: Tactic) => (tac2: Tactic) => (name: String) => (state: Goals) => (subGoalIndex: Int) => (result: TacticApplicationResult) => {
      val state1 = tac1(name)(state)(subGoalIndex)(result)
      if (state1.isEmpty) {
        tac2(name)(state)(subGoalIndex)(result)
      } else {
        state1
      }
    }

  /**
    * Tactic that always succeeds on the given state.
    * @return
    */
  def id: Tactic = (name: String) => (state: Goals) => (subGoalIndex: Int) => (result: TacticApplicationResult) => {
    Some(new TacticApplicationResult(result.getApplicationList, state))
  }

  /**
    * Tactic that always fails on the given state.
    * @return
    */
  def fail: Tactic = (name: String) => (state: Goals) => (subGoalIndex: Int) => (result: TacticApplicationResult) => {
    None
  }

  /**
    * Executes the tactic it gets as a parameter. If this tactic fails, it returns the
    * original state. This combinator never fails.
    * @return
    */
  def TRY: Tactic => Tactic = (tac : Tactic) =>  {
    ORELSE(tac)(id)
  }

  /**
    * Applies the given tactic repeatedly to the given state until it fails.
    * This combinator never fails.
    * @return
    */
  def REPEAT: Tactic => Tactic =
    (tac: Tactic) => (name: String) => (state: Goals) => (subGoalIndex: Int) => (result: TacticApplicationResult) => {
      ORELSE(THEN(tac)(REPEAT(tac)))(id)(name)(state)(subGoalIndex)(result)
    }

  /**
    * Applies the given tactic as often as the integer parameter defines, or until it fails.
    * This combinator only fails if the number of executions exceeds the integer parameter.
    * @return
    */
  def REPEAT_TIMES: Int => Tactic => Tactic = (i : Int) => (tac: Tactic) => (name: String) => (state: Goals) => (subGoalIndex: Int) => (result: TacticApplicationResult) =>
  {
    if (i<=0) {
      fail(name)(state)(subGoalIndex)(result)
    } else {
      ORELSE(THEN(tac)(REPEAT_TIMES(i-1)(tac)))(id)(name)(state)(subGoalIndex)(result)
    }
  }

  /**
    * Applies the given tactic until the predicate is true. This combinator fails if the tactic
    * cannot be applied anymore, but the predicate is still false on the given subgoal.
    * @return
    */
  def DEPTH_FIRST: GoalPredicate => Tactic => Tactic =
    (predicate: GoalPredicate) => (tac: Tactic) => (name: String) => (state: Goals) => (subGoalIndex: Int) => (result: TacticApplicationResult) => {
      if (predicate(state)(subGoalIndex)) {
        id(name)(state)(subGoalIndex)(result)
      } else {
        THEN(tac)(DEPTH_FIRST(predicate)(tac))(name)(state)(subGoalIndex)(result)
      }
    }


  /**
    * Evaluates the predicate on the current state. If it returns true, the combinator executes the first
    * tactic, otherwise it executes the second, Fails if the predicate is true and the first tactic
    * fails or if the predicate is false and the second tactic fails.
    * @return
    */
  def COND: GoalPredicate => Tactic => Tactic => Tactic =
    (p: GoalPredicate) => (tac1: Tactic) => (tac2: Tactic) => (name: String) => (state: Goals) => (subGoalIndex: Int) => (result: TacticApplicationResult) => {
      if (p(state)(subGoalIndex)) {
        tac1(name)(state)(subGoalIndex)(result)
      } else {
        tac2(name)(state)(subGoalIndex)(result)
      }
    }

  /**
    * Applies the given tactic to the current state once. Fails if the tactic fails.
    * @return
    */
  def BY: Tactic => Tactic = (tac: Tactic) => (name: String) => (state: Goals) => (subGoalIndex: Int) => (result: TacticApplicationResult) => {
    tac(name)(state)(subGoalIndex)(result)
  }

}
