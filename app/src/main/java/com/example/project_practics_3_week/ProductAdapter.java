package com.example.project_practics_3_week;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> products;
    private Context context;
    private boolean useCartIcon;
    private static final String API_KEY = BuildConfig.SUPABASE_API_KEY;
    private static final String AUTH_TOKEN = BuildConfig.SUPABASE_AUTH_TOKEN;

    public ProductAdapter(Context context, List<Product> products, boolean useCartIcon) {
        this.context = context;
        this.products = products;
        this.useCartIcon = useCartIcon;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = useCartIcon ? R.layout.item_product2 : R.layout.item_product;
        View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.title.setText(product.getName());
        holder.price.setText(String.format("₽%.2f", product.getPrice()));
        holder.tag.setText("BEST SELLER");

        String imageUrl = "https://bhvkcqjjlfvxiwbntdpy.supabase.co/storage/v1/object/public/orders/" + product.getImage_url();
        GlideUrl glideUrl = new GlideUrl(imageUrl, new LazyHeaders.Builder()
                .addHeader("apikey", API_KEY)
                .addHeader("Authorization", AUTH_TOKEN)
                .build());
        Glide.with(context)
                .load(glideUrl)
                .placeholder(R.drawable.botinok1)
                .error(R.drawable.botinok1)
                .into(holder.image);

        holder.addButton.setOnClickListener(v -> {
            Toast.makeText(context, "Added " + product.getName() + " to cart", Toast.LENGTH_SHORT).show();
        });

        holder.favoriteButton.setOnClickListener(v -> {
            Toast.makeText(context, product.getName() + " added to favorites", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() { return products.size(); }

    public void updateProducts(List<Product> newProducts) {
        products.clear();
        products.addAll(newProducts);
        notifyDataSetChanged();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView image, favoriteButton;
        TextView tag, title, price;
        ImageButton addButton;

        public ProductViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img_product);
            favoriteButton = itemView.findViewById(R.id.img_favorite);
            tag = itemView.findViewById(R.id.tv_tag);
            title = itemView.findViewById(R.id.tv_title);
            price = itemView.findViewById(R.id.tv_price);
            addButton = itemView.findViewById(R.id.btn_add);
        }
    }
}
