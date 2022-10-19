package hieuntph22081.fpoly.assignment.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hieuntph22081.fpoly.assignment.R;
import hieuntph22081.fpoly.assignment.adapter.Top10Adapter;
import hieuntph22081.fpoly.assignment.database.MyDatabase;
import hieuntph22081.fpoly.assignment.database.PhieuMuonDAO;
import hieuntph22081.fpoly.assignment.database.SachDAO;
import hieuntph22081.fpoly.assignment.model.Sach;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Top10SachFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Top10SachFragment extends Fragment {
    RecyclerView recyclerView;
    Top10Adapter adapter;
    PhieuMuonDAO phieuMuonDAO;
    SachDAO sachDAO;
    public Top10SachFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Top10SachFragment newInstance() {
        Top10SachFragment fragment = new Top10SachFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top10_sach, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerViewTop10);
        phieuMuonDAO = MyDatabase.getInstance(getContext()).phieuMuonDAO();
        sachDAO = MyDatabase.getInstance(getContext()).sachDAO();

        int[] maSaches = phieuMuonDAO.getTop10();
        List<Sach> saches = new ArrayList<>();
        for (int ms : maSaches) {
            saches.add(sachDAO.getSachByMaSach(ms));
//            Toast.makeText(getContext(), ""+ms, Toast.LENGTH_SHORT).show();
        }
        adapter = new Top10Adapter(getContext(), saches);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }
}