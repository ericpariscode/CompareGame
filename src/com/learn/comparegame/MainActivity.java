package com.learn.comparegame;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private static final String TAG = MainActivity.class.getSimpleName();
	SharedPreferences mPrefs ;
	private TextView mTextView;
	private static final String KEY_COUNT = "count";
	
	int board[][];
	Button b[][];
	TextView text;
	Gameboard game=new Gameboard();
	Button again;
	boolean isClicked=false;
	String hidden[][];
	String temp;
	int xTemp1,yTemp1,xTemp2,yTemp2; 
	int count_clicked=0;
	Button bt10,btSend;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPrefs = getPreferences(MODE_PRIVATE);
		int count = mPrefs.getInt(KEY_COUNT, 0);
		count = count + 1;
		Editor editor = mPrefs.edit();
		editor.putInt(KEY_COUNT, count);
		editor.commit();	
		setContentView(R.layout.activity_main);
		
		mTextView = (TextView)findViewById(R.id.tvCount);
		
		mTextView.setText("Count : " + count);
		
		game.setBoard();	
		again=(Button)findViewById(R.id.btAgain);
		again.setOnClickListener(handler);
		
		btSend=(Button)findViewById(R.id.btSend);
		btSend.setOnClickListener(handler);
	}
	
	
	View.OnClickListener handler = new View.OnClickListener() {
	  public void onClick(View v) {
	      switch(v.getId()) {
	      
	        case R.id.btAgain:
	          replayGame();
	          Runnable adder = new Runnable() {
					@Override
					public void run() {
						int clickCount = 1 + mPrefs.getInt("played", 0);
						mPrefs.edit().putInt("played", clickCount).putBoolean("user", true).commit();
						mTextView.setTextColor(0xff11ff11);
						mTextView.setText("PLAYED!" + clickCount);			
					}
				};
				
				mTextView.postDelayed(adder, 2000);
	          break;
	        case R.id.btSend:
	        	sendResult();
	        	break;
	      }
	  }
	};
	
	public void sendResult()
	{
		
		double m=Math.random();
		if(m<0.5)
		{
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_TEXT, "Let 's play together!!!");
			startActivity(i);
		}
		else
		{
			String comments = "let play game together";
			String phone = "0991029930";

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setData(Uri.fromParts("sms", phone, null));
			intent.putExtra("sms_body", comments);

			try {
				startActivity(intent);
			} catch (Exception ex) {
				Log.e(TAG, "Could not send message", ex);
			}
		}
	}
	
	public void replayGame()
	{
		game.setBoard();	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onResume() {
		
		super.onResume();
	}
	

	@Override
	protected void onPause() {
		
		super.onPause();
	}
	
	public boolean onOptionsItemSelected(MenuItem item)
	{
		game.setBoard();
		return true;
	}
	
	class Gameboard implements OnClickListener{
	
		int x,y;
		
		public Gameboard()
		{
			
		}
		
		public Gameboard(int x,int y)
		{
			this.x=x;
			this.y=y;
		}
		
		public void setBoard()
		{
			b=new Button[5][4];
			board=new int[5][4];
			hidden=new String[5][4];
	
			
			text=(TextView)findViewById(R.id.tv1);
			b[1][3]=(Button)findViewById(R.id.button1);
			b[1][2]=(Button)findViewById(R.id.button2);
			b[1][1]=(Button)findViewById(R.id.button3);
			
			b[2][3]=(Button)findViewById(R.id.button4);
			b[2][2]=(Button)findViewById(R.id.button5);
			b[2][1]=(Button)findViewById(R.id.button6);
			
			b[3][3]=(Button)findViewById(R.id.button7);
			b[3][2]=(Button)findViewById(R.id.button8);
			b[3][1]=(Button)findViewById(R.id.button9);
			
			b[4][3]=(Button)findViewById(R.id.button10);
			b[4][2]=(Button)findViewById(R.id.button11);
			b[4][1]=(Button)findViewById(R.id.button12);
			
			
			for(int i=1;i<=4;i++)
			{
				for(int j=1;j<=3;j++)
				{
					board[i][j]=2;
				}
			}
			
			hidden[1][1]="Q";
			hidden[1][2]="W";
			hidden[1][3]="Q";
			hidden[2][1]="W";
			hidden[2][2]="O";
			hidden[2][3]="H";
			hidden[3][1]="A";
			hidden[3][2]="A";
			hidden[3][3]="H";
			hidden[4][1]="O";
			hidden[4][2]="J";
			hidden[4][3]="J";
			
			text.setText("Press button");
			for(int i=1;i<=4;i++)
			{
				for(int j=1;j<=3;j++)
				{
					  b[i][j].setOnClickListener(new Gameboard(i, j));
	                  if(!b[i][j].isEnabled()) {
	                	  
	                       b[i][j].setText("---");
	                       b[i][j].setEnabled(true);
	                  }
				}
			}
			
		}	
		@Override
		public void onClick(View view )
		{
			
			if(b[x][y].isEnabled())
			{	
				count_clicked+=1;
				if(count_clicked==1)
				{
					xTemp1=x;
					yTemp1=y;
					b[xTemp1][yTemp1].setEnabled(true);
					b[xTemp1][yTemp1].setText(hidden[xTemp1][yTemp1]);
					board[xTemp1][yTemp1]=0;
				}
				else if(count_clicked==2)
				{
					count_clicked=0;
					xTemp2=x;
					yTemp2=y;
					b[xTemp2][yTemp2].setEnabled(true);
					if(hidden[xTemp2][yTemp2]==hidden[xTemp1][yTemp1])
					{
						b[xTemp2][yTemp2].setText(hidden[xTemp2][yTemp2]);
						board[xTemp2][yTemp2]=1;
						b[xTemp2][yTemp2].setEnabled(false);
						b[xTemp1][yTemp1].setEnabled(false);
					}
					else
					{
						b[xTemp1][yTemp1].setEnabled(true);
						b[xTemp1][yTemp1].setText("---");
						board[xTemp1][yTemp1]=2;
						board[xTemp2][yTemp2]=2;
					}	
				}
				text.setText("");
			}
			check();
		}
	  public void getContent(int x,int y)
	  {
		  if(hidden[x][y]==temp)
		  {
			  b[x][y].setText(hidden[x][y]);
			  board[x][y]=1;
			  b[x][y].setEnabled(false);
		  } 
		  else
		  {	
			  b[x][y].setText("---");
			  board[x][y]=2;
			  b[x][y].setEnabled(true);	  
		  }

	  }
	  
	  public boolean check()
	  {
		  for(int i=1;i<=4;i++)
		  {
			  for(int j=1;j<=3;j++)
			  {
				  if(board[i][j]==2)
				  {
					  text.setText("");
					  return false;
				  }
			  }
		  }
		  
		  text.setText("You win!");
		  Toast.makeText(getApplicationContext(), "Congratulation!!!", Toast.LENGTH_SHORT).show();
		  return true;
	  }
	}
}