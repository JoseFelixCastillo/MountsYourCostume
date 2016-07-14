package upsa.mimo.es.mountsyourcostume.activities;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Transition;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.adapters.TransitionAdapter;
import upsa.mimo.es.mountsyourcostume.helpers.CostumeDBHelper;
import upsa.mimo.es.mountsyourcostume.model.Costume;
import upsa.mimo.es.mountsyourcostume.model.CostumeSQLiteOpenHelper;

public class CostumeActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM = "CostumeActivity:extraItem";

    private CollapsingToolbarLayout collapsingToolbar;

    private TextView category;
    private TextView materials;
    private TextView steps;
    private TextView price;
    private ImageView image;
    private FloatingActionButton fab;

    private ViewGroup container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costume);

        loadUI();
        enableFullScreen();

        setFields((Costume) getIntent().getParcelableExtra(EXTRA_ITEM));
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

    private void loadUI(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_activity_costume);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        container = (ViewGroup) findViewById(R.id.coordinator_activity_costume);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_activity_costume);

        category = (TextView) findViewById(R.id.category_activity_costume);
        materials = (TextView) findViewById(R.id.materials_activity_costume);
        steps = (TextView) findViewById(R.id.steps_activity_costume);
        price = (TextView) findViewById(R.id.price_activity_costume);
        image = (ImageView) findViewById(R.id.image_costume);
        fab = (FloatingActionButton) findViewById(R.id.fab_activity_costume);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int deletions = deleteCostume(getTitle().toString());
                if(deletions>0){
                    Snackbar snackbar = Snackbar.make(container,"Costume eliminado",Snackbar.LENGTH_LONG);
                    snackbar.show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        fab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
                            @Override public void onHidden(FloatingActionButton fab) {
                                Intent intent = new Intent(CostumeActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        });
                        animateTitleAlpha(true);
                    }
                    else{
                        Intent intent = new Intent(CostumeActivity.this,MainActivity.class);
                        startActivity(intent);


                    }
                }
                else{
                    Snackbar snackbar = Snackbar.make(container,"No se pudo eliminar",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });

    }

    private void setFields(Costume costume){

        setTitle(costume.getName());

        category.setText(costume.getCategory());
        materials.setText(costume.getMaterials());
        steps.setText(costume.getSteps());
        price.setText(Integer.toString(costume.getPrize()));
        // Picasso.with(this).load(costume.getUri_image()).into(image);
        image.setImageURI(Uri.parse(costume.getUri_image()));



    }

    private int deleteCostume(String name){

        CostumeSQLiteOpenHelper costumeDB = CostumeSQLiteOpenHelper.getInstance(CostumeActivity.this,CostumeSQLiteOpenHelper.DATABASE_NAME,null,CostumeSQLiteOpenHelper.DATABASE_VERSION);
        SQLiteDatabase db = costumeDB.getWritableDatabase();
        int deletions = CostumeDBHelper.deleteCostumeByName(db,name);
        return deletions;
    }

    private void setAnimationHide(){
        fab.hide(new FloatingActionButton.OnVisibilityChangedListener() {
            @Override public void onHidden(FloatingActionButton fab) {
                ActivityCompat.finishAfterTransition(CostumeActivity.this);
            }
        });
        animateTitleAlpha(true);
    }
}
