package com.randomnoose.switcheroni.styles;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.randomnoose.switcheroni.R;
import com.randomnoose.switcheroni.data.Style;
import com.randomnoose.switcheroni.di.ActivityScoped;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

@ActivityScoped
public class StyleFragment extends DaggerFragment implements StyleContract.View {

  private StyleContract.Presenter presenter;

  private List<ImageButton> btn_styles = new ArrayList<>(6);

  @Inject
  public StyleFragment() {
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    final View root = inflater.inflate(R.layout.fragment_style, container, false);

    btn_styles.add(root.findViewById(R.id.btn_style_01));
    btn_styles.add(root.findViewById(R.id.btn_style_02));
    btn_styles.add(root.findViewById(R.id.btn_style_03));
    btn_styles.add(root.findViewById(R.id.btn_style_04));
    btn_styles.add(root.findViewById(R.id.btn_style_05));
    btn_styles.add(root.findViewById(R.id.btn_style_06));

    initButtonCallbacks();
    return root;
  }

  @Override
  public void onResume() {
    super.onResume();
    presenter.takeView(this);
  }

  private void initButtonCallbacks() {
    final Style[] values = Style.values();
    for (int idx = 0; idx < btn_styles.size(); idx++) {
      Picasso.get()
          .load(values[idx].getStyleId())
          .fit()
          .into(btn_styles.get(idx));
      final String type = values[idx].getType();
      btn_styles.get(idx).setOnClickListener(event -> presenter.chooseStyle(type));
    }
  }

  @Override
  public void finish(String style) {
    final Intent result = new Intent();
    result.putExtra("style", style);
    getActivity().setResult(Activity.RESULT_OK, result);
    getActivity().finish();
  }

  @Inject
  public void setPresenter(StyleContract.Presenter presenter) {
    this.presenter = presenter;
  }
}
