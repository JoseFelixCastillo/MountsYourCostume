package upsa.mimo.es.mountsyourcostume.activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
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
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.adapters.TransitionAdapter;
import upsa.mimo.es.mountsyourcostume.application.MyApplication;
import upsa.mimo.es.mountsyourcostume.dialogs.DialogConfirmDeleteCostume;
import upsa.mimo.es.mountsyourcostume.model.Costume;

public class CostumeActivity extends BaseActivity implements DialogConfirmDeleteCostume.DialogConfirmDeleteCostumeInterface{

    private static final String TAG = CostumeActivity.class.getSimpleName();
    public static final String EXTRA_ITEM = "CostumeActivity:extraItem";

    @BindView(R.id.collapsing_toolbar_activity_costume)
    CollapsingToolbarLayout collapsingToolbar;

    @BindView(R.id.category_activity_costume)
    TextView category;
    @BindView(R.id.materials_activity_costume)
    TextView materials;
    @BindView(R.id.steps_activity_costume)
    TextView steps;
    @BindView(R.id.price_activity_costume)
    TextView price;
    @BindView(R.id.image_costume)
    ImageView image;
    @BindView(R.id.coordinator_activity_costume)
    ViewGroup container;
    @BindView(R.id.toolbar_activity_costume)
    Toolbar toolbar;
    @BindView(R.id.fab_activity_costume)
    FloatingActionButton fab;
    @OnClick(R.id.fab_activity_costume)
    void eraseCostume(){
        DialogConfirmDeleteCostume dialog = DialogConfirmDeleteCostume.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        dialog.show(fm,DialogConfirmDeleteCostume.TAG);
    }

    private Costume costume;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costume);

        ButterKnife.bind(this);
        initToolbar();
        enableFullScreen();

        initFields((Costume) getIntent().getParcelableExtra(EXTRA_ITEM));

        delayAnimations();

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

    private void initFields(final Costume costume){

        this.costume = costume;

        setTitle(costume.getName());

        category.setText(costume.getCategory());
        materials.setText(costume.getMaterials());
        steps.setText(costume.getSteps());
        price.setText(String.format("%d",costume.getPrize()));
        //Dejo el codigo de palette pero al final no me gusto el resultado puesto que le daba colores raros.
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
      //  Picasso.with(this).load(new File(costume.getUri_image())).centerCrop().into(image);
    //    image.setImageURI(Uri.parse(costume.getUri_image()));

        Picasso.with(CostumeActivity.this).load(new File(costume.getUri_image())).fit().centerCrop().into(image);
      /*  ViewTreeObserver viewTreeObserver = image.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                image.getViewTreeObserver().removeOnPreDrawListener(this);
                int width,height;
                width = image.getMeasuredWidth();
                height = image.getMeasuredHeight();
            //    Log.d(TAG, "EN OBSERVER: width: "+  width + " heigth: " + height);

                Picasso.with(CostumeActivity.this).load(new File(costume.getUri_image())).resize(width,height).centerCrop().into(image);
                return true;
            }
        });*/

    }

    private int deleteCostume(String name){
        //Primero borramos la foto y despues de local
        File file = new File(costume.getUri_image());
        file.delete();
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
            Snackbar snackbar = Snackbar.make(container, R.string.costume_removed,Snackbar.LENGTH_LONG);
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
            Snackbar snackbar = Snackbar.make(container, R.string.it_could_not_removed,Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
}
