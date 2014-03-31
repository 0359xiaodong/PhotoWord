package com.dacopancm.photoword.views;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.dacopancm.photoword.helpers.Const;
import com.dacopancm.photoword.helpers.FileUtilities;
import com.dacopancm.photoword.modelItems.ColorItem;
import com.dacopancm.photoword.modelItems.TemplateItem;
import com.devsmart.android.ui.HorizontalListView;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

//66
public class TemplatesActivity extends Activity {
	final String TAG = "dcm-templates";
	private ArrayList<ColorItem> colorsdat;
	private Spinner colorpicker;
	private String wordx = "";
	private String wordres = " ";
	private String picturePath;
	private int colorselect;
	private LinearLayout progressx;
	private HorizontalListView listtemplates;
	private ArrayList<TemplateItem> templadat;
	private TemplatesAdapter templatesAdapter;
	// /xml
	private int MAX_LENGTH = 140;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_templates);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey("wordx")) {
				wordx = bundle.getString("wordx");
				picturePath = bundle.getString("imgsrc");
				Log.e(TAG, "wordx is: " + wordx);
			}
		} else {
			wordx = "";
		}

		progressx = (LinearLayout) findViewById(R.id.progressx);
		// leno colores para spinner
		colorsdat = new ArrayList<ColorItem>();
		fillColors();
		// color por defecto
		colorselect = 0x9F000000;
		// ListView
		listtemplates = (HorizontalListView) findViewById(R.id.listtemplates);
		templadat = new ArrayList<TemplateItem>();
		/*
		 * encuentro palabras en segundo plano y cuando termine lleno los
		 * templates y notifico al adaptador
		 */
		findWordTask tarea = new findWordTask();
		tarea.execute(wordx);

		templatesAdapter = new TemplatesAdapter(this);
		listtemplates.setAdapter(templatesAdapter);
		listtemplates.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				createImgToShare(v);
			}

		});
		// ColorPicker
		colorpicker = (Spinner) findViewById(R.id.colorpic);
		ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(this);
		colorpicker.setAdapter(colorPickerAdapter);
		colorpicker
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							android.view.View v, int position, long id) {
						changeColor(colorsdat.get(position));
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
	}

	/**
	 * Llena los templates, se ejecuta cuando se culmina la tarea en segundo
	 * plano
	 */
	private void fillTemplates() {
		// TODO Auto-generated method stub
		templadat.add(new TemplateItem(getString(R.string.theme_0_1) + " "
				+ wordx, wordres, 0));
		templadat.add(new TemplateItem(getString(R.string.theme_0_1) + " "
				+ wordx, wordres, 1));
		templadat.add(new TemplateItem(getString(R.string.theme_0_1) + " "
				+ wordx, getString(R.string.theme_0_2) + wordres, 2));
		templadat.add(new TemplateItem(getString(R.string.theme_0_1) + " "
				+ wordx, getString(R.string.theme_0_2) + wordres, 3));
		templadat.add(new TemplateItem(getString(R.string.theme_0_1) + " "
				+ wordx, getString(R.string.theme_0_2) + wordres, 4));
		templadat.add(new TemplateItem(getString(R.string.theme_0_1) + " "
				+ wordx, getString(R.string.theme_0_2) + wordres, 5));
		templadat.add(new TemplateItem(getString(R.string.theme_0_1) + wordx,
				wordres, 6));
		templadat.add(new TemplateItem(getString(R.string.theme_0_1) + wordx,
				getString(R.string.theme_0_2) + wordres, 7));

	}

	/**
	 * Cambia el color de los templates. Se ejecuta cuando evento spinner
	 * 
	 * @param colorItem
	 */
	private void changeColor(ColorItem colorItem) {
		int color = colorItem.getC();
		this.colorselect = Color.argb(159, Color.red(color),
				Color.green(color), Color.blue(color));
		templatesAdapter.notifyDataSetChanged();
	}

	/**
	 * Llena los colores para el spinner pickColor
	 */
	private void fillColors() {
		colorsdat.add(new ColorItem(Color.BLACK, "Black"));
		colorsdat.add(new ColorItem(Color.MAGENTA, "Magenta"));
		colorsdat.add(new ColorItem(Color.parseColor("#A200FF"), "Purple"));
		colorsdat.add(new ColorItem(Color.parseColor("#00ABA9"), "teal"));
		colorsdat.add(new ColorItem(Color.parseColor("#8CBF26"), "lime"));
		colorsdat.add(new ColorItem(Color.parseColor("#A05000"), "brown"));
		colorsdat.add(new ColorItem(Color.parseColor("#E671B8"), "pink"));
		colorsdat.add(new ColorItem(Color.parseColor("#F09609"), "orange"));
		colorsdat.add(new ColorItem(Color.parseColor("#1BA1E2"), "blue"));
		colorsdat.add(new ColorItem(Color.parseColor("#E51400"), "red"));
		colorsdat.add(new ColorItem(Color.parseColor("#339933"), "green"));
		colorsdat.add(new ColorItem(Color.parseColor("#F09609"), "mango"));

	}

	/**
	 * Convierte un SVG a PictureDrawable
	 * 
	 * @param graphic
	 *            numero de SVG a convertir
	 * @return SVG convertido en PictureDrawable
	 */
	private PictureDrawable getSVG(int graphic) {
		try {
			SVG svg = SVGParser.getSVGFromAsset(getAssets(), "theme_" + graphic
					+ ".svg", 0xff232323, colorselect);
			Picture picture = (svg.getPicture());
			return new PictureDrawable(picture);
		} catch (Exception ex) {
			Log.e(TAG, "error:" + ex.getMessage());
		}
		return ((PictureDrawable) getResources()
				.getDrawable(R.drawable.example));
	}

	/**
	 * Convierte un drawable a Bitmap
	 * 
	 * @param pictureDrawable
	 * @return
	 */
	private static Bitmap pictureDrawable2Bitmap(PictureDrawable pictureDrawable) {
		Bitmap bitmap = Bitmap.createBitmap(
				pictureDrawable.getIntrinsicWidth(),
				pictureDrawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawPicture(pictureDrawable.getPicture());
		return bitmap;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.templates, menu);
		return true;
	}

	/**
	 * Adaaptador para los templates
	 * 
	 * @author DarwinAlejandro
	 * 
	 */
	class TemplatesAdapter extends ArrayAdapter<TemplateItem> {

		Activity context;

		TemplatesAdapter(Activity context) {
			super(context, R.layout.templates_item_0, templadat);
			this.context = context;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			Log.e(TAG, "position is: " + position);
			View item = convertView;

			if (position == 0 || position == 1 || position == 6) {
				ViewHolderTemplates1 holder;
				if (item == null
						|| !(item.getTag() instanceof ViewHolderTemplates1)) {

					LayoutInflater inflater = context.getLayoutInflater();
					switch (position) {
					case 1: {
						item = inflater
								.inflate(R.layout.templates_item_1, null);
						break;
					}
					case 0: {
						item = inflater
								.inflate(R.layout.templates_item_0, null);
						break;
					}
					case 6: {
						item = inflater
								.inflate(R.layout.templates_item_6, null);
						break;
					}
					}

					holder = new ViewHolderTemplates1();

					holder.texto1 = (TextView) item.findViewById(R.id.texto1);
					holder.texto2 = (TextView) item.findViewById(R.id.texto2);
					holder.picx = (ImageView) item.findViewById(R.id.picx);
					item.setTag(holder);
				} else {
					Log.e(TAG, "item holder is? " + item.getTag().getClass()
							+ " position is: " + position);
					holder = (ViewHolderTemplates1) item.getTag();
				}
				if (Cache.add == null) {
					Cache.add = BitmapFactory.decodeFile(picturePath);
				}

				holder.texto1.setText(templadat.get(position).getText1());
				holder.texto2.setText(templadat.get(position).getText2());
				holder.texto1.setBackgroundColor(colorselect);
				holder.texto2.setBackgroundColor(colorselect);
				holder.picx.setImageBitmap(Cache.add);
			} else {
				ViewHolderTemplates2 holder;
				if (item == null
						|| !(item.getTag() instanceof ViewHolderTemplates2)) {
					holder = new ViewHolderTemplates2();

					LayoutInflater inflater = context.getLayoutInflater();

					item = inflater.inflate(R.layout.templates_item_2, null);

					holder.texto1 = (TextView) item.findViewById(R.id.texto1);
					holder.picx = (ImageView) item.findViewById(R.id.picx);
					holder.texto1.setText(templadat.get(position).getText1());
					holder.geometry = (ImageView) item
							.findViewById(R.id.geometry);
					item.setTag(holder);
				} else {
					Log.e(TAG, "item holder is? " + item.getTag().getClass()
							+ " position is: " + position);
					holder = (ViewHolderTemplates2) item.getTag();
				}

				if (Cache.add == null) {
					Cache.add = BitmapFactory.decodeFile(picturePath);
				}

				holder.texto1.setText(templadat.get(position).getText2());
				holder.picx.setImageBitmap(Cache.add);
				holder.geometry
						.setImageBitmap(pictureDrawable2Bitmap((getSVG(templadat
								.get(position).getGraphic()))));
			}
			return (item);
		}
	}

	/**
	 * Adaptador para el spinner seleccionar color
	 * 
	 * @author DarwinAlejandro
	 * 
	 */
	class ColorPickerAdapter extends ArrayAdapter<ColorItem> implements
			SpinnerAdapter {

		Activity context;

		ColorPickerAdapter(Activity context) {
			super(context, 0, colorsdat);
			this.context = context;
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			// super.getDropDownView(position, convertView, parent);

			return getView(position, convertView, parent);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View item = convertView;
			ViewHolder holder;

			if (item == null) {
				LayoutInflater inflater = context.getLayoutInflater();
				item = inflater.inflate(R.layout.color_picker_item, null);

				holder = new ViewHolder();
				holder.colorname = (TextView) item.findViewById(R.id.colorname);
				holder.clorox = item.findViewById(R.id.clorox);
				item.setTag(holder);
			} else {
				holder = (ViewHolder) item.getTag();
			}
			holder.colorname.setText(colorsdat.get(position).getName());
			holder.clorox.setBackgroundColor(colorsdat.get(position).getC());
			return (item);
		}
	}

	static class ViewHolder {
		TextView colorname;
		View clorox;
	}

	static class ViewHolderTemplates {
		TextView texto1;
		ImageView picx;
	}

	static class ViewHolderTemplates1 extends ViewHolderTemplates {
		TextView texto2;
	}

	static class ViewHolderTemplates2 extends ViewHolderTemplates {
		ImageView geometry;
	}

	static class Cache {
		static Bitmap add = null;
	}

	// Tarea Asíncrona para encontrar palabras en segundo plano
	/**
	 * Encuentra las palabras que se forman con las letras de otra dada en
	 * segundo plano
	 * 
	 * @author DarwinAlejandro
	 * 
	 */
	private class findWordTask extends AsyncTask<String, Integer, Boolean> {

		protected Boolean doInBackground(String... params) {
			Log.e("dcm-xml", "doinbackgrpund");
			//
			try {
				ToogleProgressBar(true);
				List<String> _reslult = new ArrayList<String>();
				String word1 = params[0].toLowerCase(Locale.getDefault())
						.replace("á", "a").replace("é", "e").replace("í", "i")
						.replace("ó", "o").replace("ú", "u");
				Log.e("dcm-xml", "word1 ist: " + word1);
				List<String> dic = new ArrayList<String>();

				char[] word1list = word1.toCharArray();
				Arrays.sort(word1list);
				Log.e("dcm-xml", "word1list sort: " + word1list.toString());
				String dicName = word1list.length + "l.dic";

				Log.e(TAG, "diccionario a leer: " + dicName);
				dic = leerDIC(dicName);
				Log.e(TAG, "diccionario leido: " + dic.size());

				for (String dicItem : dic) {
					String word2 = (dicItem.toLowerCase(Locale.getDefault())
							.replace("á", "a").replace("é", "e")
							.replace("í", "i").replace("ó", "o").replace("ú",
							"u"));

					char[] word2list = word2.toCharArray();
					Arrays.sort(word2list);
					if (ConstainsAll(word1list, word2list))
						_reslult.add(dicItem);
				}

				Log.e("dcm-xml", "_reslult: " + _reslult);
				String _answer = "";
				for (String _r : _reslult) {
					if ((_answer.length() + _r.length() + 2) < MAX_LENGTH)
						_answer += "#" + _r + " ";
				}
				if (_answer.length() > 0) {
					wordres += _answer;
				} else {
					wordres += "#no find word";
				}
			} catch (Exception ex) {
				wordres += "no find word";
				Log.e(TAG, "Error en tarea segundo plano");
			}
			return true;
		}

		/**
		 * activa o desactiva el progressbar mientras carga listView templates
		 * 
		 * @param b
		 */
		private void ToogleProgressBar(boolean b) {
			if (b) {
				progressx.setVisibility(View.VISIBLE);
			} else {
				progressx.setVisibility(View.INVISIBLE);
			}

		}

		protected void onPostExecute(Boolean result) {
			// lleno templates
			fillTemplates();
			// togle progressbar
			ToogleProgressBar(false);
			// notifico al adatador de los listview del templates q estoy listo.
			templatesAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * Compara si todos los elelemntos de una lista estan en otro
	 * 
	 * @param word1list
	 *            lista a comparar
	 * @param word2list
	 *            lista a comparar
	 * @return true si la listas son iguales
	 */
	private boolean ConstainsAll(char[] word1list, char[] word2list) {
		for (int i = 0; i < word1list.length; i++) {
			if (word1list[i] != word2list[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Lee un xml diccionario y devuelve una lista de palabras Nota: <strong>Muy
	 * demoorooosooo...</strong>
	 * 
	 * @param filename
	 *            archivo a leer
	 * @return lista de palabras deserealizadas
	 */
	private List<String> leerDIC(String filename) {
		List<String> list = new ArrayList<String>();
		try {

			BufferedReader fin = new BufferedReader(new InputStreamReader(
					getResources().getAssets().open("dics/" + filename)));

			String dic = fin.readLine();
			fin.close();
			list = Arrays.asList(dic.split("&"));

		} catch (Exception e) {
			Log.e("DCM-XML", e.getMessage());
			list = new ArrayList<String>();
		}
		return list;
	}

	private void createImgToShare(View v) {
		Log.e(TAG, "ctreando imagen to share");
		Bitmap bmp = FileUtilities.getBitmapFromView(v);
		FileOutputStream fOut = null;
		try {
			Log.e(TAG, "file imagen to share created");
			fOut = new FileOutputStream(FileUtilities.createImageFile(
					TemplatesActivity.this, Const.PHOTOWORD_FILE_1));
			Log.e(TAG, "FileOutputStream created");
			bmp.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			Log.e(TAG, "Bitmap final created");

			//
			String imageFileName = Const.PHOTOWORD_FILE_1;
			File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
			File image = new File(storageDir, imageFileName);
			//
			Intent IShare = new Intent(TemplatesActivity.this,
					SharexActivity.class);
			IShare.putExtra("filename", image.getAbsolutePath());
			IShare.putExtra("wordx", wordx);
			IShare.putExtra("wordres", wordres);
			startActivityForResult(IShare, Const.GO_BACK_REQUEST);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, "createImgToShare error: " + e.getMessage());
		} finally {
			try {
				fOut.flush();
				fOut.close();
			} catch (Throwable ignore) {
				Log.e("dcm", "exception2" + ignore.getMessage());
			}
		}

	}
}
