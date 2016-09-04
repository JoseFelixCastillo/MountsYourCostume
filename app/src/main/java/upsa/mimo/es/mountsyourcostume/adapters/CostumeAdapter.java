package upsa.mimo.es.mountsyourcostume.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import upsa.mimo.es.mountsyourcostume.R;
import upsa.mimo.es.mountsyourcostume.model.Costume;

/**
 * Created by JoseFelix on 18/05/2016.
 */
public class CostumeAdapter extends RecyclerView.Adapter<CostumeAdapter.CostumeViewHolder>{


    private static final String TAG = "COSTUME_ADAPTER";

    private final List<Costume> costumes;
    private OnItemClickListener listener;
    private Context context;


    public interface OnItemClickListener {
        void onItemClick(View view, Costume costume);
    }

    public CostumeAdapter(List<Costume> costumes, OnItemClickListener listener, Context context){

        this.costumes = costumes;
        this.listener = listener;
        this.context = context;

    }
    @Override
    public CostumeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.costume_item,parent,false);

        return new CostumeViewHolder(itemView,context);
    }

    @Override
    public void onBindViewHolder(CostumeViewHolder holder, int position) {

       // Log.d(TAG,"mostrando costume: " + costumes.get(position).getName());
        holder.bindCostume(costumes.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return costumes.size();
    }



    public class CostumeViewHolder extends RecyclerView.ViewHolder{

        private TextView textViewCategoryItemViewHolder;
        private ImageView imageViewItemViewHolder;
      //  private TextView textViewNameItemViewHolder;
        private Context context;

        public CostumeViewHolder(View itemView,Context context) {
            super(itemView);

            imageViewItemViewHolder = (ImageView) itemView.findViewById(R.id.image_costume);

         //   textViewNameItemViewHolder = (TextView) itemView.findViewById(R.id.text_view_name_item_recycler_view);
            textViewCategoryItemViewHolder = (TextView) itemView.findViewById(R.id.text_view_category_item_recicler_view);

            this.context = context;

        }

        public void bindCostume(final Costume costume, final OnItemClickListener listener) {
         //   textViewNameItemViewHolder.setText(costume.getName());

            textViewCategoryItemViewHolder.setText(costume.getCategory());
            Picasso.with(context).load(new File(costume.getUri_image())).fit().centerCrop().into(imageViewItemViewHolder);
           // setImageWithOberserver(costume);
           // Log.d(TAG, "BIND: width: "+  width + " heigth: " + height);

            //imageViewItemViewHolder.setImageURI(Uri.parse(costume.getUri_image()));

           // Picasso.with(context).load(new File(costume.getUri_image())).fit().centerCrop().into(imageViewItemViewHolder);
         //   Log.d(TAG, "BIND: width: "+  imageViewItemViewHolder.getWidth() + " heigth: " + imageViewItemViewHolder.getHeight());
           // Log.d(TAG, "bindeamos en el view holder");
            //this.costume = costume;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(itemView,costume);
                }
            });
        }
        //De este forma se reduce el consumo de bytes al colocar fotos
       /* private void setImageWithOberserver(final Costume costume){
            ViewTreeObserver viewTreeObserver = imageViewItemViewHolder.getViewTreeObserver();
            viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    imageViewItemViewHolder.getViewTreeObserver().removeOnPreDrawListener(this);
                    int width,height;
                    width = imageViewItemViewHolder.getMeasuredWidth();
                    height = imageViewItemViewHolder.getMeasuredHeight();
                  //  Log.d(TAG, "EN OBSERVER: width: "+  width + " heigth: " + height);

                    Picasso.with(context).load(new File(costume.getUri_image())).resize(width,height).centerCrop().into(imageViewItemViewHolder);
                    return true;
                }
            });
        }*/
    }
}
