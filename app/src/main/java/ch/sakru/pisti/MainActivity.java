package ch.sakru.pisti;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import ch.sakru.pisti.game.PistiGameController;
import ch.sakru.pisti.gui.PistiAnimationCoordinator;
import ch.sakru.pisti.gui.PistiBoard;
import ch.sakru.pisti.settings.PistiSettingsDialog;
import ch.sakru.pisti.settings.PistiSettingsStore;

/** Android entry point: wires the board, settings, game and animation flow together. */
public class MainActivity extends AppCompatActivity {
    private PistiGameController game;
    private PistiAnimationCoordinator animations;
    private PistiSettingsStore settingsStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PistiBoard board = new PistiBoard(this,
                view -> animations.onHumanCardClicked((ImageView) view));
        game = new PistiGameController(getApplicationContext(), board);
        animations = new PistiAnimationCoordinator(this, game, board);
        settingsStore = new PistiSettingsStore(this);
        game.applySettings(settingsStore.load());

        findViewById(R.id.iv_settings).setOnClickListener(view ->
                new PistiSettingsDialog().show(this, settingsStore.load(), settings -> {
                    settingsStore.save(settings);
                    game.applySettings(settings);
                }));
        findViewById(R.id.iv_help).setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), HelpActivity.class)));

        game.newGame();
        animations.onRoundStarted();
    }

    @Override
    protected void onDestroy() {
        animations.destroy();
        super.onDestroy();
    }
}
