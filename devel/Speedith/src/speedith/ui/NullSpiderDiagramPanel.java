/*
 *   Project: Speedith
 * 
 * File name: NullSpiderDiagramPanel.java
 *    Author: Matej Urbas [matej.urbas@gmail.com]
 * 
 *  Copyright © 2011 Matej Urbas
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

/*
 * NullSpiderDiagramPanel.java
 *
 * Created on 29-Sep-2011, 13:46:22
 */
package speedith.ui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.UIManager;
import speedith.core.lang.NullSpiderDiagram;

/**
 * This panel displays {@link NullSpiderDiagram null spider diagrams}.
 *
 * @author Matej Urbas [matej.urbas@gmail.com]
 */
public class NullSpiderDiagramPanel extends javax.swing.JPanel {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private boolean highlighting = false;
    private static final Color DefaultColor = new Color(0, 0, 0);
    private static final Color HighlightColor = new Color(0xff, 0, 0);
    private static final String TopSymbol = "\u22A4";
    private Font defaultFont;
    private Font highlightFont;
    // </editor-fold>

    /**
     * Creates new form NullSpiderDiagramPanel
     */
    public NullSpiderDiagramPanel() {
        this(null);
    }

    /**
     * Creates new form NullSpiderDiagramPanel
     * @param font the font to use to display the top symbol with.
     */
    public NullSpiderDiagramPanel(Font font) {
        initComponents();
        if (font == null) {
            font = getFont();
        }
        resetFont(font);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblNullSD = new javax.swing.JLabel();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(25, 33));
        setLayout(new java.awt.BorderLayout());

        lblNullSD.setForeground(DefaultColor);
        lblNullSD.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNullSD.setText(TopSymbol);
        lblNullSD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onLabelClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                onMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                onMouseEntered(evt);
            }
        });
        add(lblNullSD, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void onMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onMouseExited
        if (isHighlighting()) {
            applyNoHighlight();
        }
    }//GEN-LAST:event_onMouseExited

    private void onMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onMouseEntered
        if (isHighlighting()) {
            applyHighlight();
        }
    }//GEN-LAST:event_onMouseEntered

    private void onLabelClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onLabelClicked
        dispatchEvent(evt);
    }//GEN-LAST:event_onLabelClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel lblNullSD;
    // End of variables declaration//GEN-END:variables

    // <editor-fold defaultstate="collapsed" desc="Public Methods">
    public void setHighlighting(boolean enable) {
        if (enable != highlighting) {
            this.highlighting = enable;
            if (!this.highlighting) {
                applyNoHighlight();
            }
        }
    }

    public boolean isHighlighting() {
        return highlighting;
    }
    // </editor-fold>

    
    // <editor-fold defaultstate="collapsed" desc="Overrides">
    @Override
    public void setFont(Font font) {
        super.setFont(font);
        resetFont(font);
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Private Helper Methods">
    private void applyNoHighlight() {
        lblNullSD.setFont(defaultFont);
        lblNullSD.setForeground(DefaultColor);
    }

    private void applyHighlight() {
        lblNullSD.setFont(highlightFont);
        lblNullSD.setForeground(HighlightColor);
    }

    private void resetFont(Font font) {
        if (font == null) {
            // NOTE: Maybe we should get the default font? Something like:
            // UIManager.getDefaults().getFont("Label.font");
            font = new Font(Font.DIALOG, Font.PLAIN, 12);
        }
        defaultFont = font.deriveFont(24f);
        highlightFont = font.deriveFont(Font.BOLD, 26f);
        lblNullSD.setFont(defaultFont);
    }
    // </editor-fold>
}