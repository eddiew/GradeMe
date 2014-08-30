package net.eddiew.grademe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by WangE on 8/30/2014.
 */
public class SelectTestDFragment extends DialogFragment {
    int select;

    public interface SelectTestDListener {
        public void onSDialogPositiveClick(DialogFragment dialog);
        public void onSDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    SelectTestDListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SelectTestDListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement SelectTestDListener");
        }
    }

    public void setSelect(int select) {
        this.select = select;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton("Grade Test", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Calls grading method
                        mListener.onSDialogPositiveClick(SelectTestDFragment.this);
                    }
                })
                .setNegativeButton("Print Test", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Calls printing method
                        mListener.onSDialogNegativeClick(SelectTestDFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

