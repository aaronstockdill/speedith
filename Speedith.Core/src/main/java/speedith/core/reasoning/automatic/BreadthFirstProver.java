package speedith.core.reasoning.automatic;

import speedith.core.lang.SpiderDiagram;
import speedith.core.reasoning.Goals;
import speedith.core.reasoning.Proof;
import speedith.core.reasoning.ProofTrace;
import speedith.core.reasoning.RuleApplicationException;
import speedith.core.reasoning.automatic.rules.PossibleRuleApplication;
import speedith.core.reasoning.automatic.strategies.NoStrategy;
import speedith.core.reasoning.automatic.strategies.Strategy;
import speedith.core.reasoning.automatic.wrappers.SpiderDiagramWrapper;
import speedith.core.reasoning.rules.util.AutomaticUtils;

import java.util.*;

/**
 * @author Sven Linker [s.linker@brighton.ac.uk]
 */
public class BreadthFirstProver extends  AutomaticProver {

    private static final String proverName = "breadth_first";

    public BreadthFirstProver() {super(new NoStrategy());}

    public BreadthFirstProver(Strategy strategy) {
        super(strategy);
    }

    @Override
    protected Proof prove(Proof p, int subgoalindex, AppliedRules appliedRules) throws RuleApplicationException {
        p = tryToFinish(p, subgoalindex);
        if (p.isFinished()) {
            return p;
        }
        // initialise the set of proofs to be considered
        Set<ProofTrace> currentProofs = new HashSet<>();
        currentProofs.add((ProofTrace) p);
        // initialise the new set of proofs
        Set<ProofTrace> newProofs = new HashSet<>();
        Map<ProofTrace, AppliedRules> rulesWithinProofs = new HashMap<>();
        rulesWithinProofs.put((ProofTrace) p, appliedRules);

         Goals currentGoals = p.getLastGoals();
        // get all names of contours present in the goals. This bounds the
        // possible proof rule applications, since contours not in this set
        // never have to be copied or introduced.
        Collection<String> contours = new HashSet<String>();
        for (SpiderDiagram sd : currentGoals.getGoals()) {
            contours.addAll( AutomaticUtils.collectContours(sd));
        }
        Proof finishedProof = null;
        PossibleRuleApplication nextRule = null;
        while(finishedProof == null && !currentProofs.isEmpty()) {
            newProofs = new HashSet<>();
            for(ProofTrace current : currentProofs) {
                current = (ProofTrace) tryToFinish(current, subgoalindex);
                if (current.isFinished()) {
                    // we found a finished proof
                    finishedProof = current;
                    break;
                } else {
                    // create all possible proof rules for this unfinished proof
                    SpiderDiagramWrapper target = wrapDiagram(current.getLastGoals().getGoalAt(subgoalindex),0);
                    Set<PossibleRuleApplication> applications = AutomaticUtils.createAllPossibleRuleApplications(target, contours, rulesWithinProofs.get(current));
                    nextRule = null;
                    // apply all possible rules to the current proof, creating a new proof for each application
                    do {
                        ProofTrace newCurrent = new ProofTrace(current.getGoals(), current.getRuleApplications());
                        // create a new set of already applied rules for the current proof
                        AppliedRules alreadyApplied = new AppliedRules(rulesWithinProofs.get(current));
                        nextRule = getStrategy().select(newCurrent, applications);
                        boolean hasbeenApplied = nextRule != null && nextRule.apply(newCurrent, subgoalindex, appliedRules);
                        if (hasbeenApplied) {
                            // save the new proof within the set of not yet considered proofs
                            newProofs.add(newCurrent);
                            // save the rules that have already been applied for the new proof
                            rulesWithinProofs.put(newCurrent,alreadyApplied);
                        }
                    } while(nextRule !=null);
                }
                // remove the applied rules for this proof (since we create a new proof for each step)
                rulesWithinProofs.remove(current);
            }
            // the new set of not yet considered proofs
            currentProofs = newProofs;
        }
        return finishedProof;
    }

    @Override
    public AutomaticProver getAutomaticProver() {
        return this;
    }

    @Override
    public String getAutomaticProverName() {
        return proverName;
    }

    @Override
    public String getDescription() {
        return "Breadth first proof search";
    }

    @Override
    public String getPrettyName() {
        return "Breadth First";
    }
}
