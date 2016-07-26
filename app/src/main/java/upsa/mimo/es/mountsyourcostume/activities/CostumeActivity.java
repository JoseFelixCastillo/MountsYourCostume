package upsa.mimo.es.mountsyourcostume.activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.adapters.TransitionAdapter;
import upsa.mimo.es.mountsyourcostume.application.MyApplication;
import upsa.mimo.es.mountsyourcostume.dialogs.DialogConfirmDeleteCostume;
import upsa.mimo.es.mountsyourcostume.model.Costume;

public class CostumeActivity extends BaseActivity implements DialogConfirmDeleteCostume.DialogConfirmDeleteCostumeInterface{

    public static final String EXTRA_ITEM = "CostumeActivity:extraItem";

    @BindView(R.id.collapsing_toolbar_activity_costume)
    CollapsingToolbarLayout collapsingToolbar;
    //private CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.category_activity_costume)
    TextView category;
   // private TextView category;
    @BindView(R.id.materials_activity_costume)
    TextView materials;
   // private TextView materials;
    @BindView(R.id.steps_activity_costume)
    TextView steps;
  //  private TextView steps;
    @BindView(R.id.price_activity_costume)
    TextView price;
  //  private TextView price;
    @BindView(R.id.image_costume)
    ImageView image;
 //   private FloatingActionButton fab;
    @BindView(R.id.coordinator_activity_costume)
    ViewGroup container;
    @BindView(R.id.toolbar_activity_costume)
    Toolbar toolbar;

    //  private ViewGroup container;
    //   private ImageView image;
    @BindView(R.id.fab_activity_costume)
    FloatingActionButton fab;
    @OnClick(R.id.fab_activity_costume)
    void eraseCostume(){
        DialogConfirmDeleteCostume dialog = DialogConfirmDeleteCostume.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        dialog.show(fm,DialogConfirmDeleteCostume.TAG);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costume);

        ButterKnife.bind(this);
        initToolbar();
        enableFullScreen();

        initFields((Costume) getIntent().getParcelableExtra(EXTRA_ITEM));
        //Para el fab ver si es bueno
        delayAnimations();
     //   fab.show();
    }

    private void enableFullScreen() {
        // Let views draw behind status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void delayAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().addListener(new TransitionAdapter() {
                @Override public void onTransitionEnd(Transition transition) {
                    fab.show();
                    animateTitleAlpha(false);
                }
            });
        }
        else{
            fab.show();
        }
    }

    //Comproabar a partir de aqui a ver que hace
    private void animateTitleAlpha(boolean reverse) {
        ValueAnimator animator;
        if (reverse) {
            animator = ObjectAnimator.ofInt(255, 0);
        } else {
            animator = ObjectAnimator.ofInt(0, 255);
        }

        animator.setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                collapsingToolbar.setExpandedTitleColor(getWhiteWithAlpha(value));
            }
        });

        animator.start();
    }

    private int getWhiteWithAlpha(int alpha) {
        return Color.argb(alpha, 255, 255, 255);
    }


    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setAnimationHide();
        }
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setAnimationHide();
            }
            else{
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initToolbar(){

        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void initFields(Costume costume){

        setTitle(costume.getName());

        category.setText(costume.getCategory());
        materials.setText(costume.getMaterials());
        steps.setText(costume.getSteps());
        price.setText(String.format("%d",costume.getPrize()));
       // price.setText(Integer.toString(costume.getPrize()));
       /* Target target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                image.setImageBitmap(bitmap);
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {

                        int contentScrimColor = palette.getVibrantColor(ContextCompat.getColor(CostumeActivity.this,R.color.colorPrimary));
                        int statusScrimColor = palette.getDarkVibrantColor(ContextCompat.getColor(CostumeActivity.this,R.color.colorPrimaryDark));
                        int expandedTitleColor = palette.getMutedColor(ContextCompat.getColor(CostumeActivity.this,R.color.primary_text));
                        int collapsedTitleColor = palette.getLightMutedColor(ContextCompat.getColor(CostumeActivity.this,R.color.primary_light));

                        collapsingToolbar.setContentScrimColor(contentScrimColor);
                        collapsingToolbar.setStatusBarScrimColor(statusScrimColor);
                        collapsingToolbar.setExpandedTitleColor(expandedTitleColor);
                        collapsingToolbar.setCollapsedTitleTextColor(collapsedTitleColor);
                    }
                });
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };*/
      //  Picasso.with(this).load(new File(costume.getUri_image())).into(target);
        image.setImageURI(Uri.parse(costume.getUri_image()));

    }

    private int deleteCostume(String name){

        return MyApplication.getLocalPersistance().deleteCostume(name);

    }

    private void setAnimationHide(){
        fab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override public void onHidden(FloatingActionButton fab) {
                ActivityCompat.finishAfterTransition(CostumeActivity.this);
            }
        });
        animateTitleAlpha(true);
    }

    @Override
    public void confirmDelete() {
        int deletions = deleteCostume(getTitle().toString());
        if(deletions>0){
            Snackbar snackbar = Snackbar.make(container,"Costume eliminado",Snackbar.LENGTH_LONG);
            snackbar.show();
            final Intent intent = new Intent(CostumeActivity.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setExitTransition(new Explode());
                fab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                    @Override public void onHidden(FloatingActionButton fab) {

                        startActivity(intent);
                        finish();
                    }
                });
                animateTitleAlpha(true);
            }
            else{
                startActivity(intent);
                finish();
            }
        }
        else{
            Snackbar snackbar = Snackbar.make(container,"No se pudo eliminar",Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
