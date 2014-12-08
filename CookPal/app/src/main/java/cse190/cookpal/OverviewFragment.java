package cse190.cookpal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

/**
 * Created by timchi on 11/28/14.
 */
public class OverviewFragment extends Fragment {

    private Bitmap bitmap;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.overview_tab, container, false);

        Recipe currRecipe = RecipeActivity.getCurrentRecipe();
        final String imageURL = currRecipe.getImgUrl();
        final ImageView recipeImage = (ImageView) rootView.findViewById(R.id.overview_recipe_image);

       new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageURL).getContent());
                } catch (Exception e) {
                    // log error
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if (bitmap != null) {
                    recipeImage.setImageBitmap(bitmap);
                } else {
                    recipeImage.setImageResource(R.drawable.placeholder);
                }

            }

        }.execute();


        //recipeImage.setImageResource(R.drawable.lasagna);

        TextView recipeName = (TextView) rootView.findViewById(R.id.overview_recipe_name);
        recipeName.setText(currRecipe.getRecipeName());
        recipeName.setBackground(getResources().getDrawable(R.drawable.overview_title_card));

        return rootView;
    }
}
