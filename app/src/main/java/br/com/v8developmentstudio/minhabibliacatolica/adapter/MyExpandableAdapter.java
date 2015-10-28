package br.com.v8developmentstudio.minhabibliacatolica.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.v8developmentstudio.minhabibliacatolica.MainActivity;
import br.com.v8developmentstudio.minhabibliacatolica.R;
import br.com.v8developmentstudio.minhabibliacatolica.vo.Capitulo;
import br.com.v8developmentstudio.minhabibliacatolica.vo.Livro;

public class MyExpandableAdapter extends BaseExpandableListAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<Livro> livroArrayList;
    private List<Capitulo> childCapitulos;
    private MainActivity mainActivity;
    private TextView textView = null;

    public MyExpandableAdapter(ArrayList<Livro> livros, MainActivity mainActivity) {
        this.livroArrayList = livros;
        this.mainActivity = mainActivity;
    }


    public void setInflater(LayoutInflater inflater, Activity activity) {
        this.inflater = inflater;
        this.activity = activity;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        childCapitulos = livroArrayList.get(groupPosition).getCapituloList();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group, null);
        }

        textView = (TextView) convertView.findViewById(R.id.tvItemGrupo);
        textView.setText(childCapitulos.get(childPosition).getTitulo());

        convertView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //  Toast.makeText(activity, childCapitulos.get(childPosition).getTitulo(), Toast.LENGTH_SHORT).show();
                String itemsData[] = activity.getResources().getStringArray(R.array.CapGen2);
                mainActivity.createView(mainActivity.getRecyclerView(), itemsData);
                mainActivity.getDrawer().closeDrawers();
            }
        });

        return convertView;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row, null);
        }

        ((CheckedTextView) convertView).setText(livroArrayList.get(groupPosition).getTituloLivro());
        ((CheckedTextView) convertView).setChecked(isExpanded);

        return convertView;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return  livroArrayList.get(groupPosition).getCapituloList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public int getGroupCount() {
        return livroArrayList.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}