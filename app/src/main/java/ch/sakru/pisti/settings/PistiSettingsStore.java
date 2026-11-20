package ch.sakru.pisti.settings;

import android.content.Context;
import android.content.SharedPreferences;

import ch.sakru.pisti.R;

public final class PistiSettingsStore {
    private final Context context;

    public PistiSettingsStore(Context context) {
        this.context = context.getApplicationContext();
    }

    private SharedPreferences preferences() {
        return context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    public PistiSettings load() {
        SharedPreferences saved = preferences();
        String name = saved.getString(context.getString(R.string.player_name), context.getString(R.string.player_name));
        int target = 51;
        // Keep the existing preference keys and their precedence for installed games.
        if (saved.getBoolean(context.getString(R.string.max_51), true)) {
            target = 51;
        } else if (saved.getBoolean(context.getString(R.string.max_101), false)) {
            target = 101;
        } else if (saved.getBoolean(context.getString(R.string.max_151), false)) {
            target = 151;
        }
        return new PistiSettings(name, target);
    }

    public void save(PistiSettings settings) {
        preferences().edit()
                .putString(context.getString(R.string.player_name), settings.getPlayerName())
                .putBoolean(context.getString(R.string.max_51), settings.getPointsToReach() == 51)
                .putBoolean(context.getString(R.string.max_101), settings.getPointsToReach() == 101)
                .putBoolean(context.getString(R.string.max_151), settings.getPointsToReach() == 151)
                .apply();
    }
}
