package ch.sakru.pisti.settings;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import ch.sakru.pisti.BuildConfig;
import ch.sakru.pisti.R;

public final class PistiSettingsDialog {
    public interface OnSave {
        void save(PistiSettings settings);
    }

    public void show(Activity activity, PistiSettings current, OnSave onSave) {
        Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_settings);

        TextView version = dialog.findViewById(R.id.version);
        version.setText("Version: " + BuildConfig.VERSION_NAME);
        EditText name = dialog.findViewById(R.id.username);
        RadioButton max51 = dialog.findViewById(R.id.max_51);
        RadioButton max101 = dialog.findViewById(R.id.max_101);
        RadioButton max151 = dialog.findViewById(R.id.max_151);
        name.setText(current.getPlayerName());
        switch (current.getPointsToReach()) {
            case 101:
                max101.setChecked(true);
                break;
            case 151:
                max151.setChecked(true);
                break;
            default:
                max51.setChecked(true);
                break;
        }

        Button save = dialog.findViewById(R.id.button_save);
        save.setOnClickListener(view -> {
            int target = max101.isChecked() ? 101 : max151.isChecked() ? 151 : 51;
            onSave.save(new PistiSettings(name.getText().toString(), target));
            dialog.dismiss();
        });
        dialog.show();
    }
}
