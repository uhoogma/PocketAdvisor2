package com.example.pocketadvisor2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * ToDoListAdapter.java
 * Tegu on klassiga mida kasutatakse listi vaate loomiseks ja selle
 * valikuliseks va''rvimiseks
 * @version 0.1
 * @author  Arun Krishna
 * @author 	Jonathan Simon
 * @author  Urmas Hoogma
 * @since   1.5
 */

public class ToDoListAdapter extends BaseAdapter {
	/** Konstant ma''a''ramaks kindlaks tiitli ja kirjelduse eelvaate
	 * maksimaalset pikkust, et need yhele reale a''ra mahuksid  */
	public static final int PREVIEW_MAX_LENGTH = 18;

	/** Vaate kontekst */
	Context context;

	/** ArrayList ToDo objektidega */
	ArrayList<ToDo> todoList;

	/**
	 * Klassi ToDoListAdapter vaate loov meetod
    * @param context Vaate kontekst
    * @param list List mida ta''idetakse ja osaliselt va''rvitakse
    */
	public ToDoListAdapter(Context context, ArrayList<ToDo> list) {
		this.context = context;
		todoList = list;
   }

	/**
	 * @return Listi suurus
	 */
   @Override
   public int getCount() {
   	return todoList.size();
   }

   /**
    * @param position Listi itemi positsioon
	 * @return Parameetrile position vastav listi item
	 */
   @Override
   public Object getItem(int position) {
      return todoList.get(position);
   }

   /**
    * @param position Listi itemi positsioon
	 * @return Parameetrile position vastav listi itemi positsioon andmetyybiga
	 * long
	 */
   @Override
   public long getItemId(int position) {
      return position;
   }

   /**
	 * Listi t2itev meetod. Listi sisestatkse yks item korraga.
    * @param position Listi itemi positsioon
    * @param convertView Vaade mida tuleb ta''ita
    * @param arg2 hetkel kasutamata
    */
   @Override
   public View getView(int position, View convertView, ViewGroup arg2) {
   	/** hangime positsiooni ja''rgi ToDo objekti */
   	ToDo todoItems = todoList.get(position);
      if (convertView == null) {
      	/** Listi itemi vaate seadmine */
      	LayoutInflater inflater = (LayoutInflater) context
      		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.to_do_list_item2, null);
      }
      /** ToDo kuupa''eva kysimine */
      String tododate = todoItems.getDate();
      /** kalender millesse pannakse ToDo kuupa''ev */
		Calendar todocal = Calendar.getInstance();
		/** Kuupaevavorming */
	   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	   /** ToDo kuupa''eva panemine kalendrisse  */
	   try {
	      todocal.setTime(sdf.parse(tododate));
      } catch (ParseException e) {
	      e.printStackTrace();
      }
	   /** ToDo itemi aasta */
      String yeartodo = Integer.toString(todocal.get(Calendar.YEAR)) ;
	   /** ToDo itemi kuu */
		String monthtodo = Integer.toString(todocal.get(Calendar.MONTH)+1);
	   /** ToDo itemi pa''ev */
	   String datetodo = Integer.toString(todocal.get(Calendar.DATE));
	   /** Pa''eva ja kuu konkatenatsioon */
      String datestring = datetodo +"/"+ monthtodo;

      /** Tekstivaate kuupa''ev (sisaldab nii pa''eva kui kuud) leidmine */
      TextView date = (TextView) convertView.findViewById(R.id.datenr);
      /** Tekstivaate aasta leidmine */
      TextView year = (TextView) convertView.findViewById(R.id.yearnr);
      /** Tekstivaate tiitel leidmine */
      TextView title = (TextView) convertView.findViewById(R.id.title);
      /** Tekstivaate kirjeldus leidmine */
      TextView desc = (TextView) convertView.findViewById(R.id.description);

      /** Hetke aja muutmine SQLite formaadis kuupa''evaks */
      Calendar d = Calendar.getInstance();
		int x = d.get(Calendar.YEAR);
      int y = d.get(Calendar.MONTH)+1;
		int z = d.get(Calendar.DATE);

		/** Praegune kuupa''eva stringi kujul */
		String dfh = String.valueOf(x)+"-"+String.valueOf(y)+"-"+
			String.valueOf(z);

		/** Kontroll: Overdue ToDo-d  (mis on yhtlasi aktiivsed) va''rvitakse
		 * punaseks, ta''nased ja tulevad roheliseks */
		if (dfh.compareTo(tododate) > 0 ) {
			date.setBackgroundColor(context.getResources().getColor(R.color.
					overdue_color));
			year.setBackgroundColor(context.getResources().getColor(R.color.
					overdue_color));
      } else if (dfh.compareTo(tododate) <= 0) {
      	date.setBackgroundColor(context.getResources().getColor(R.color.
      			upcoming_color));
			year.setBackgroundColor(context.getResources().getColor(R.color.
					upcoming_color));
      }
		String titleuncut = todoItems.getTitle();
		String descuncut = todoItems.getDescription();
		String titleshort = (titleuncut.length() >= PREVIEW_MAX_LENGTH) ?
				(titleuncut.substring(0, PREVIEW_MAX_LENGTH)) + "..." :
					titleuncut.substring(0, titleuncut.length()) ;
		String descshort = (descuncut.length() >= PREVIEW_MAX_LENGTH) ?
				(descuncut.substring(0, PREVIEW_MAX_LENGTH)) + "..." :
					descuncut.substring(0, descuncut.length());
		/** Listi itemi vaate va''ljade ta''itmine */
      date.setText(datestring);
      year.setText(yeartodo);
      title.setText(titleshort);
      desc.setText(descshort);

      return convertView;
    }

}
