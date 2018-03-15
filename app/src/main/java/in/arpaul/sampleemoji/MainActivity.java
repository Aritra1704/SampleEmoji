package in.arpaul.sampleemoji;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.widget.EmojiAppCompatTextView;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import static android.widget.LinearLayout.HORIZONTAL;

public class MainActivity extends AppCompatActivity {

    // [U+1F469] (WOMAN) + [U+200D] (ZERO WIDTH JOINER) + [U+1F4BB] (PERSONAL COMPUTER)
    private static final String WOMAN_TECHNOLOGIST = "\uD83D\uDC69\u200D\uD83D\uDCBB";

    // [U+1F469] (WOMAN) + [U+200D] (ZERO WIDTH JOINER) + [U+1F3A4] (MICROPHONE)
    private static final String WOMAN_SINGER = "\uD83D\uDC69\u200D\uD83C\uDFA4";

    static final String EMOJI = WOMAN_TECHNOLOGIST + " " + WOMAN_SINGER;

    final String[] arrEmojis = new String[]{"\u263A",
            "\uD83D\uDC69",
            "\uD83D\uDCBB",
            "\uD83D\uDC69",
            "\uD83C\uDFA4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // TextView variant provided by EmojiCompat library
        final TextView emojiTextView = findViewById(R.id.emoji_text_view);
        emojiTextView.setText(getString(R.string.emoji_text_view, EMOJI));

        // EditText variant provided by EmojiCompat library
        final TextView emojiEditText = findViewById(R.id.emoji_edit_text);
        emojiEditText.setText(getString(R.string.emoji_edit_text, EMOJI));

        // Button variant provided by EmojiCompat library
        final TextView emojiButton = findViewById(R.id.emoji_button);
        emojiButton.setText(getString(R.string.emoji_button, EMOJI));

//        final TextView regularTextView = findViewById(R.id.regular_text_view);
//        EmojiCompat.get().registerInitCallback(new InitCallback(regularTextView));

        final Button btnPopUp = findViewById(R.id.btnPopUp);
        btnPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View popView = LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_emoji, null);
                RecyclerView rvEmoji = popView.findViewById(R.id.rvEmoji);
                rvEmoji.setLayoutManager(new LinearLayoutManager(MainActivity.this, HORIZONTAL, false));
                rvEmoji.setAdapter(new EmojiAdapter());

                PopupWindow popupWindow = new PopupWindow();
                popupWindow = new PopupWindow(
                        popView,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    popupWindow.setElevation(5.0f);
                }

                popupWindow.showAsDropDown(btnPopUp);
//                popupWindow.showAtLocation(btnPopUp, Gravity.END, 0, 0);
//                PopupWindowCompat.showAsDropDown(popupWindow, btnPopUp, 0, 0, Gravity.END);
            }
        });
    }

    private static class InitCallback extends EmojiCompat.InitCallback {

        private final WeakReference<TextView> mRegularTextViewRef;

        InitCallback(TextView regularTextView) {
            mRegularTextViewRef = new WeakReference<>(regularTextView);
        }

        @Override
        public void onInitialized() {
            final TextView regularTextView = mRegularTextViewRef.get();
            if (regularTextView != null) {
                final EmojiCompat compat = EmojiCompat.get();
                final Context context = regularTextView.getContext();
                regularTextView.setText(
                        compat.process(context.getString(R.string.regular_text_view, EMOJI)));
            }
        }

    }

    class EmojiAdapter extends RecyclerView.Adapter<EmojiAdapter.EmojiViewHolder> {

        @Override
        public EmojiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.emoji_cell, null);

            return new EmojiViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return arrEmojis.length;
        }

        @Override
        public void onBindViewHolder(EmojiViewHolder holder, final int position) {
            holder.tvEmoji.setText(arrEmojis[position]);

            holder.tvEmoji.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "Position: " + position, Toast.LENGTH_SHORT).show();
                }
            });
        }

        public class EmojiViewHolder extends RecyclerView.ViewHolder {
            public final EmojiAppCompatTextView tvEmoji;

            public EmojiViewHolder(View cell) {
                super(cell);
                tvEmoji = (EmojiAppCompatTextView) cell.findViewById(R.id.tvEmoji);
            }
        }
    }
}
