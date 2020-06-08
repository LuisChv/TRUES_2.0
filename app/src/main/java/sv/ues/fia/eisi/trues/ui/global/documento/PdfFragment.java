package sv.ues.fia.eisi.trues.ui.global.documento;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import sv.ues.fia.eisi.trues.R;

public class PdfFragment extends Fragment {

    private View view;
    private String enlace;

    public static PdfFragment newInstance() {
        return new PdfFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pdf, container, false);

        enlace = getArguments().getString("url", null);

        if (enlace != null){
            String url = "http://docs.google.com/gview?embedded=true&url=" + enlace;
            String doc="<iframe src='"+url+"' width='100%' height='100%' style='border: none;'></iframe>";

            WebView webView = view.findViewById(R.id.webViewpdf);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadData(doc, "text/html", "UTF-8");
        }

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}