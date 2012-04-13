/*
 * File name: GoalsTopComponent.java
 *    Author: matej
 * 
 *  Copyright © 2012 Matej Urbas
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package diabelli.ui;

import diabelli.Diabelli;
import diabelli.GoalsManager;
import diabelli.logic.Formula;
import diabelli.logic.Goal;
import diabelli.logic.Goals;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.TreeTableView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;
import org.openide.util.lookup.Lookups;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//diabelli.ui//Goals//EN",
autostore = false)
@TopComponent.Description(preferredID = "GoalsTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "output", openAtStartup = true)
@ActionID(category = "Window", id = "diabelli.ui.GoalsTopComponent")
@ActionReference(path = "Menu/Window" /*
 * , position = 333
 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_GoalsAction",
preferredID = "GoalsTopComponent")
@Messages({
    "CTL_GoalsAction=Diabelli Goals",
    "CTL_GoalsTopComponent=Diabelli Goals",
    "HINT_GoalsTopComponent=This window displays the list of current Diabelli goals."
})
public final class GoalsTopComponent extends TopComponent implements ExplorerManager.Provider {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private ExplorerManager em;
    private Lookup lookup;
    private GoalsChangedListenerImpl goalsChangedListener;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructors">
    public GoalsTopComponent() {
        initComponents();
        setName(Bundle.CTL_GoalsTopComponent());
        setToolTipText(Bundle.HINT_GoalsTopComponent());

        this.em = new ExplorerManager();
        ActionMap map = this.getActionMap();
        InputMap keys = this.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        this.lookup = ExplorerUtils.createLookup(this.em, map);
        this.associateLookup(this.lookup);
        
        // Make the root node invisible in the view:
        ((TreeTableView)goalsView).setRootVisible(false);
        
        updateGoalsList(null);
    }

    //<editor-fold defaultstate="collapsed" desc="Generated Code">
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        goalsView = new TreeTableView();

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(goalsView, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(goalsView, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane goalsView;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="TopComponent-Specific Stuff">
    @Override
    public void componentOpened() {
        // Register to 'goal change' events in Diabelli's goal manager:
        if (goalsChangedListener == null) {
            goalsChangedListener = new GoalsChangedListenerImpl();
        }
        GoalsManager goalManager = Lookup.getDefault().lookup(Diabelli.class).getGoalManager();
        goalManager.addPropertyChangeListener(goalsChangedListener, GoalsManager.CurrentGoalsChangedEvent);
    }

    @Override
    public void componentClosed() {
        // Unregister to 'goal change' events in Diabelli's goal manager:
        if (goalsChangedListener != null) {
            GoalsManager goalManager = Lookup.getDefault().lookup(Diabelli.class).getGoalManager();
            goalManager.addPropertyChangeListener(goalsChangedListener, GoalsManager.CurrentGoalsChangedEvent);
        }
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Goal Explorer Nodes">
    @Override
    public ExplorerManager getExplorerManager() {
        return em;
    }
    
    private static class GoalChildrenFactory extends ChildFactory<GoalWrapper> {

        private final Goals goals;

        public GoalChildrenFactory(Goals goals) {
            this.goals = goals;
        }

        @Override
        protected boolean createKeys(List<GoalWrapper> toPopulate) {
            if (goals != null && !goals.isEmpty()) {
                for (int i = 0; i < goals.size(); i++) {
                    toPopulate.add(new GoalWrapper(goals.get(i), i));
                }
            }
            return true;
        }

        @Override
        protected Node createNodeForKey(GoalWrapper key) {
            return new GoalNode(key.goal, key.index);
        }
    }

    private static class GoalWrapper {

        private final Goal goal;
        private final int index;

        public GoalWrapper(Goal goal, int index) {
            this.goal = goal;
            this.index = index;
        }
    }

    public static class GoalNode extends AbstractNode {

        private final Goal goal;
        private final int index;

        /**
         *
         * @param goal
         * @param index
         */
        public GoalNode(Goal goal, int index) {
            super(Children.create(new GoalPremisesConclusionFactory(goal), false), Lookups.singleton(goal));
            this.goal = goal;
            this.index = index;
            setDisplayName("Goal #" + (index + 1));
        }
    }

    private static class GoalPremisesConclusionFactory extends ChildFactory<FormulaWrapper> {

        private final Goal goal;

        public GoalPremisesConclusionFactory(Goal goal) {
            this.goal = goal;
        }

        @Override
        protected boolean createKeys(List<FormulaWrapper> toPopulate) {
            if (goal != null) {
                List<Formula> premises = goal.getPremises();
                if (premises != null) {
                    int i = 0;
                    for (Formula formula : premises) {
                        toPopulate.add(new FormulaWrapper(formula, i));
                    }
                }
                if (goal.getConclusion() != null) {
                    toPopulate.add(new FormulaWrapper(goal.getConclusion()));
                }
            }
            return true;
        }

        @Override
        protected Node createNodeForKey(FormulaWrapper key) {
            return new FormulaNode(key);
        }
    }

    private static class FormulaWrapper {

        private final int premiseIndex;
        private final Formula formula;

        public FormulaWrapper(Formula premise, int premiseIndex) {
            this.formula = premise;
            this.premiseIndex = premiseIndex;
        }

        public FormulaWrapper(Formula conclusion) {
            this(conclusion, -1);
        }

        public boolean isPremise() {
            return premiseIndex >= 0;
        }

        public Formula getFormula() {
            return formula;
        }

        public String getPrettyFormatName() {
            return formula == null ? "No formula!" : formula.getMainRepresentation().getFormat().getPrettyName();
        }

        public String getDisplayName() {
            return (isPremise() ? ("Premise #" + (premiseIndex + 1) + ": ") : "Conclusion: ") + getPrettyFormatName();
        }
    }

    private static class FormulaNode extends AbstractNode {

        public FormulaNode(FormulaWrapper key) {
            super(Children.LEAF);
            setName(key.toString());
            setDisplayName(key.getDisplayName());
        }
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Event Handlers">
    private class GoalsChangedListenerImpl implements PropertyChangeListener {

        private GoalsChangedListenerImpl() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updateGoalsList((Goals)evt.getNewValue());
        }
    }
    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="UI Refresh Methods">
    private void updateGoalsList(Goals goals) {
        Children children = Children.create(new GoalChildrenFactory(goals), false);
        Node root = new AbstractNode(children);
        this.em.setRootContext(root);
        this.em.getRootContext().setDisplayName("Diabelli Goals List");
    }
    // </editor-fold>
}
