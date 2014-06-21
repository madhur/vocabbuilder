package in.co.madhur.vocabbuilder.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.TabHost;

import in.co.madhur.vocabbuilder.Consts;
import in.co.madhur.vocabbuilder.R;

/**
 * Created by madhur on 19-Jun-14.
 */
public class AboutDialog extends DialogFragment
{

    private WebView aboutWebView, whatsNewView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.about_dialog, null);
        builder.setView(v);

        TabHost tabHost = (TabHost) v.findViewById(R.id.about_tab);
        aboutWebView = (WebView) v.findViewById(R.id.about_webview);
        whatsNewView = (WebView) v.findViewById(R.id.whatsnew_webview);

        tabHost.setup();

        builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();

            }
        });

        builder.setPositiveButton(R.string.feedback_button, new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", Consts.MY_EMAIL, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.feedback_button));
                startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)));

            }
        });

        builder.setNegativeButton(R.string.rate_button, new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Uri uri = Uri.parse("market://details?id="
                        + getActivity().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try
                {
                    startActivity(goToMarket);
                }
                catch (ActivityNotFoundException e)
                {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="
                            + getActivity().getPackageName())));
                }

            }
        });

        TabHost.TabSpec aboutTab = tabHost.newTabSpec(Consts.ABOUT_TAG);
        aboutTab.setIndicator(getString(R.string.action_about));
        aboutTab.setContent(R.id.tab1);

        TabHost.TabSpec whatsnewTab = tabHost.newTabSpec(Consts.WHATS_NEW_TAG);
        whatsnewTab.setIndicator(getString(R.string.whatsnew_tab));
        whatsnewTab.setContent(R.id.tab2);

        tabHost.addTab(aboutTab);
        tabHost.addTab(whatsnewTab);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener()
        {

            @Override
            public void onTabChanged(String tabId)
            {

            }
        });

        aboutWebView.loadUrl(Consts.ABOUT_URL);
        whatsNewView.loadUrl(Consts.CHANGES_URL);


        return builder.create();
    }


}
