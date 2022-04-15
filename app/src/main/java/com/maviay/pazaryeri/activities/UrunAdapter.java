package com.maviay.pazaryeri.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maviay.pazaryeri.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UrunAdapter extends BaseAdapter {
  List<Urun> urunler = new ArrayList<Urun>();
  Context context;
  public UrunAdapter(Context context) {
    this.context = context;
  }


  public void add(Urun urun) {
    this.urunler.add(urun);
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return urunler.size();
  }

  @Override
  public Object getItem(int position) {
    return urunler.get(position);
  }

  @Override
  public long getItemId(int position) {
    return urunler.get(position).getId();
  }

  @Override
  public View getView(int i, View convertView, ViewGroup viewGroup) {
    MessageViewHolder holder = new MessageViewHolder();
    LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    Urun message = urunler.get(i);

      convertView = messageInflater.inflate(R.layout.urun_liste, null);
      holder.ivSol2 = (ImageView) convertView.findViewById(R.id.ivSol2);
      holder.ivSag = (ImageView) convertView.findViewById(R.id.ivSag);
      holder.tvSatir1a = (TextView) convertView.findViewById(R.id.tvSatir1a);
      holder.tvSatir1b = (TextView) convertView.findViewById(R.id.tvSatir1b);
      holder.tvSatir2a = (TextView) convertView.findViewById(R.id.tvSatir2a);
      holder.tvSatir2b = (TextView) convertView.findViewById(R.id.tvSatir2b);
      holder.tvSatir3a = (TextView) convertView.findViewById(R.id.tvSatir3a);
      holder.tvSatir3b = (TextView) convertView.findViewById(R.id.tvSatir3b);
      holder.tvSatir4a = (TextView) convertView.findViewById(R.id.tvSatir4a);
      holder.tvSatir4b = (TextView) convertView.findViewById(R.id.tvSatir4b);
      holder.tvSatir5a = (TextView) convertView.findViewById(R.id.tvSatir5a);
      convertView.setTag(holder);
      holder.tvSatir1a.setText(message.getTitle());
      holder.urunId= message.getId();
      holder.tvSatir1b.setText("ID: " + holder.urunId.toString());
      holder.tvSatir2a.setText(message.getDescription());
      holder.tvSatir2b.setText("");
      holder.tvSatir3a.setText(message.getBrand());
      holder.tvSatir3b.setText("");
      holder.tvSatir4a.setText(message.getCategory());
      holder.tvSatir4b.setText(message.getPrice().toString() + " TL");
      holder.tvSatir5a.setText("");
      if (message.getThumbnail() != null) {
        String thumb = message.getThumbnail();
        //new UrunAdapter.DownloadImageTask((ImageView) holder.ivSol2).execute(message.getThumbnail());
        Glide.with(context).load(message.getThumbnail()).into((ImageView) holder.ivSol2);
      }
      holder.ivSag.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
          Intent intent = new Intent(context, UrunActivity.class);
          intent.putExtra("id", holder.urunId);
          context.startActivity(intent);
        }
      });


      //GradientDrawable drawable = (GradientDrawable) holder.avatar.getBackground();
      //drawable.setColor(Color.parseColor(message.getMemberData().getColor()));

    return convertView;
  }
  private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> { // Kullanılmıyor.
    ImageView bmImage;
    public DownloadImageTask(ImageView bmImage) {
      this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
      String urldisplay = urls[0];
      Bitmap mIcon11 = null;
      try {
        InputStream in = new java.net.URL(urldisplay).openStream();
        mIcon11 = BitmapFactory.decodeStream(in);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
      bmImage.setImageBitmap(result);
    }
  }
}

class MessageViewHolder {
  public ImageView ivSol2, ivSag;
  public TextView tvSatir1a, tvSatir1b, tvSatir2a, tvSatir2b, tvSatir3a, tvSatir3b, tvSatir4a, tvSatir4b, tvSatir5a;
  public Long urunId;
}