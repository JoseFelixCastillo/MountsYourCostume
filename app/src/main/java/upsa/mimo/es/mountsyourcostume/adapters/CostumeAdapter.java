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

            textViewCategoryItemViewHolder = (TextView) itemView.findViewById(R.id.text_view_category_item_recicler_view);

            this.context = context;

        }

        public void bindCostume(final Costume costume, final OnItemClickListener listener) {

            textViewCategoryItemViewHolder.setText(costume.getCategory());
            Picasso.with(context).load(new File(costume.getUri_image())).fit().centerCrop().into(imageViewItemViewHolder);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(itemView,costume);
                }
            });
        }

    }
}
