package com.globalsovy.carserviceapp.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.globalsovy.carserviceapp.MainActivity;
import com.globalsovy.carserviceapp.R;
import com.google.android.material.navigation.NavigationView;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.List;

public class PDF_fragment extends Fragment {

    Toolbar toolbar;
    NavigationView navigationView;
    TextView toolbarTitle;
    ImageView toolbarBtn;

    String pdfPath;



    public String SAMPLE_FILE;
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_pdf,container,false);

        navigationView = parent.findViewById(R.id.nav_view);
        toolbarTitle = getActivity().findViewById(R.id.toolbarTitle);
        toolbarBtn = getActivity().findViewById(R.id.toolbarTool);
        toolbar = getActivity().findViewById(R.id.toolbar);
        pdfPath = ((MainActivity)getActivity()).getUrlPdf();
        pdfView = (PDFView) parent.findViewById(R.id.pdf_viewer);

        ((MainActivity)getActivity()).setNavigationButtonToDefault();

        toolbar.setNavigationIcon(R.drawable.ic_keyboard_backspace_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).changeFragment(Car_Details_fragment.class);
            }
        });
        toolbarBtn.setVisibility(View.GONE);
        pdfView.fromFile(new File(pdfPath)).defaultPage(1).enableSwipe(true).load();
        return parent;
    }

}