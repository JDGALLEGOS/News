package com.galpotechsolutions.news;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class NewAdapter extends ArrayAdapter<New> {

    public NewAdapter(Activity context, ArrayList<New> details) {
        super(context, 0, details);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_items, parent, false);

            //Find the TextView in the list_items.xml layout with the ID title_text_view
            viewHolder.titleTextView = listItemView.findViewById(R.id.title_text_view);

            //Find the TextView in the list_items.xml layout with the ID title_text_view
            viewHolder.sectionTextView = listItemView.findViewById(R.id.section_text_view);

            //Find the TextView in the list_items.xml layout with the ID title_text_view
            viewHolder.publicationDateTextView = listItemView.findViewById(R.id.publicationDate_text_view);

            //Find the TextView in the list_items.xml layout with the ID title_text_view
            viewHolder.autorTextView = listItemView.findViewById(R.id.authorName_text_view);

            //Find the TextView in the list_items.xml layout with the ID title_text_view
            viewHolder.bodieTextView = listItemView.findViewById(R.id.description_text_view);

            //Find the ImageView in the list_items.xml layout with the ID title_text_view
            viewHolder.imageView = listItemView.findViewById(R.id.imageOfNewsArticle_text_view);
            //Set the tag of the ViewHolder
            listItemView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) listItemView.getTag();
        }

        // Get the {@link Detail} object located at this position in the list
        New currentDetail =getItem(position);

        // Get the title of the new from the current new object and set this text to the New title
        viewHolder.titleTextView.setText(currentDetail.getTitle());

        // Get the section of the new from the current new object and set this text to the New section
        viewHolder.sectionTextView.setText(currentDetail.getSection());

        // Get the pulication date of the new from the current new object and set this text to the New publication date
        viewHolder.publicationDateTextView.setText(currentDetail.getPublicationDate());

        // Get the autor of the new from the current new object and set this text to the New writer
        viewHolder.autorTextView.setText(currentDetail.getAutor());

        // Get the body of the new from the current new object and set this text to the New body
        viewHolder.bodieTextView.setText(currentDetail.getBody());

        // Get the image of the new from the current new object and set this imageResource to the New image
        String imageUrl = currentDetail.getImageResourceIds();

        // Check if an image is provided for this new story or not
        if (currentDetail.hasImage()){
            // Display the image of the current new story in that ImageView
            new DownloadImageTask(viewHolder.imageView).execute(imageUrl);
        }
        //viewHolder.imageView.setImageResource(currentDetail.getImageResourceIds());

        return listItemView;
    }

    static class ViewHolder{
        TextView titleTextView;
        TextView sectionTextView;
        TextView publicationDateTextView;
        TextView autorTextView;
        //TextView urlTextView;
        TextView bodieTextView;
        ImageView imageView;
    }

    // Inner Class for downloading images
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;

         DownloadImageTask(ImageView imageView) {

            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                // Catch the download exception
                Log.v("download", e.getMessage());

            }
            return logo;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}
