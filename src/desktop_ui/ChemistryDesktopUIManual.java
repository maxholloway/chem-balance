package desktop_ui;

import equation_solver.Solutions;

public class ChemistryDesktopUIManual extends javax.swing.JFrame {

    public ChemistryDesktopUIManual() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        reactionTextField = new javax.swing.JTextField();
        balanceFromManualButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        outputBox = new javax.swing.JTextArea();
        returnToMenuButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("Enter Reaction:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, -1, -1));

        reactionTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reactionTextFieldActionPerformed(evt);
            }
        });
        getContentPane().add(reactionTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 90, 650, -1));
        reactionTextField.getAccessibleContext().setAccessibleName("");
        reactionTextField.getAccessibleContext().setAccessibleDescription("");

        balanceFromManualButton.setText("Balance!");
        balanceFromManualButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                balanceFromManualButtonActionPerformed(evt);
            }
        });
        getContentPane().add(balanceFromManualButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 90, -1, -1));

        outputBox.setColumns(20);
        outputBox.setRows(5);
        jScrollPane1.setViewportView(outputBox);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 760, 50));

        returnToMenuButton.setText("<- Go Back");
        returnToMenuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnToMenuButtonActionPerformed(evt);
            }
        });
        getContentPane().add(returnToMenuButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void reactionTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reactionTextFieldActionPerformed
        // TODO add your handling code here:
        System.out.println(evt.getActionCommand());
        String possibleReaction = evt.getActionCommand();
        
        String solution = Solutions.getManualSolutionString(possibleReaction);
        
        outputBox.setText(solution);
    }//GEN-LAST:event_reactionTextFieldActionPerformed

    private void balanceFromManualButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_balanceFromManualButtonActionPerformed
        // get the text from the reaction that they input
        String textEntered = reactionTextField.getText();

        String solution = Solutions.getManualSolutionString(textEntered);
        
        outputBox.setText(solution);
    }//GEN-LAST:event_balanceFromManualButtonActionPerformed

    private void returnToMenuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnToMenuButtonActionPerformed
        desktop_ui.ChemistryDesktopUIMenu.setReturnToMenu(true);
        this.dispose();
    }//GEN-LAST:event_returnToMenuButtonActionPerformed


    
    public void initiateFrame() {
        /* Set the OS X look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Mac OS X".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChemistryDesktopUIManual.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ChemistryDesktopUIManual().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton balanceFromManualButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea outputBox;
    private javax.swing.JTextField reactionTextField;
    private javax.swing.JButton returnToMenuButton;
    // End of variables declaration//GEN-END:variables
}
