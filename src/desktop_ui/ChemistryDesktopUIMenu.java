package desktop_ui;

public class ChemistryDesktopUIMenu extends javax.swing.JFrame {
    private static String option;
    protected static boolean returnToMenu;
    
    // sets if it will return to the menu
    protected static void setReturnToMenu(boolean b){
        returnToMenu = b;
    }
    
    /**
     * Creates new form ChemistryDesktopUI
     */
    public ChemistryDesktopUIMenu() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        InputButtonGroup = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        MethodOfInputComboBox = new javax.swing.JComboBox<>();
        startEnteringReactionOrPath = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        MethodOfInputComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Manual", "Textfile" }));
        MethodOfInputComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MethodOfInputComboBoxActionPerformed(evt);
            }
        });

        startEnteringReactionOrPath.setText("Go");
        startEnteringReactionOrPath.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startEnteringReactionOrPathActionPerformed(evt);
            }
        });

        jLabel2.setText("Method of entering equations:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(MethodOfInputComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(startEnteringReactionOrPath)
                .addContainerGap(670, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(MethodOfInputComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(startEnteringReactionOrPath))
                .addContainerGap(307, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MethodOfInputComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MethodOfInputComboBoxActionPerformed
        // TODO add your handling code here:        
    }//GEN-LAST:event_MethodOfInputComboBoxActionPerformed

    private void startEnteringReactionOrPathActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startEnteringReactionOrPathActionPerformed
        // TODO add your handling code here:
        option = (String)MethodOfInputComboBox.getSelectedItem();
        //this.dispose();
        if(option.equals("Textfile")) {
            returnToMenu = false;
            ChemistryDesktopUITextfile cText = new ChemistryDesktopUITextfile();
            cText.initiateFrame();
            if(returnToMenu){
                main(new String[1]);
            }
        } else if(option.equals("Manual")) {
            returnToMenu = false;
            ChemistryDesktopUIManual cManual = new ChemistryDesktopUIManual();
            cManual.initiateFrame();
            if(returnToMenu){
                main(new String[1]);
            }
        }
        
    }//GEN-LAST:event_startEnteringReactionOrPathActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ChemistryDesktopUIMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChemistryDesktopUIMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChemistryDesktopUIMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChemistryDesktopUIMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new ChemistryDesktopUIMenu().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup InputButtonGroup;
    private javax.swing.JComboBox<String> MethodOfInputComboBox;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JButton startEnteringReactionOrPath;
    // End of variables declaration//GEN-END:variables
}