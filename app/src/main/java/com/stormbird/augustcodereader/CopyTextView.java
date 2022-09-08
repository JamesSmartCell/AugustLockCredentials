package com.stormbird.augustcodereader;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class CopyTextView extends LinearLayout {

    public static final String KEY_ADDRESS = "key_address";

    private final Context context;
    private Button button;
    private TextView viewText;

    public CopyTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.context = context;

        inflate(context, R.layout.item_copy_textview, this);

        bindViews();
    }


    private void bindViews()
    {
        viewText = findViewById(R.id.view_text);
        button = findViewById(R.id.button);
        button.setOnClickListener(v -> copyToClipboard());
    }

    public String getText()
    {
        return button.getText().toString();
    }

    public void setText(CharSequence text)
    {
        if (TextUtils.isEmpty(text))
        {
            setVisibility(View.GONE);
        }
        else
        {
            setVisibility(View.VISIBLE);
            viewText.setText(text.toString());
        }
    }

    private void copyToClipboard()
    {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(KEY_ADDRESS, viewText.getText().toString());
        if (clipboard != null)
        {
            clipboard.setPrimaryClip(clip);
        }

        Toast.makeText(context, R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }
}
