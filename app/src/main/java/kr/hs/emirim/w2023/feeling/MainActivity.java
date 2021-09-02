package kr.hs.emirim.w2023.feeling;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase db;
    EditText editText;
    ListView listView;
    ArrayAdapter<String> adapter;
    List<String> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.et);
        listView = findViewById(R.id.listView);
        list = new ArrayList<>();
        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
//파일이름,허용범위,팩토리 사용유무
        db = openOrCreateDatabase("testdb.db",MODE_PRIVATE,null);
        Log.d("Sqllite","testdb 데이터베이스 생성 완료!");
        String sql = "create table if not exists test (idx integer primary key, title varchar(10))";
        db.execSQL(sql);
        Log.d("Sqllite","test테이블 생성 완료!");

        select();

    }

    public void insert(View view) {
        String data = editText.getText().toString();
        if (data != null && data.trim().length()>0) {
            String sql = "insert into test (title) values ('" + data + "')";
            db.execSQL(sql);
            Log.d("Sqllite","test테이블에 " + data + " 저장 완료!");
            editText.setText("");
            editText.requestFocus();//커서 옮기기

            select();
        }
    }

    private void select() {
        String sql = "select * from test order by idx";
        Cursor c1 = db.rawQuery(sql,new String[]{});

        list.clear();//리스트 비우기
        while (c1.moveToNext()) {
            String dbText = c1.getInt(0) + ". "; //idx번호
            dbText += c1.getString(c1.getColumnIndex("title"));

            list.add(dbText);
        }
        adapter.notifyDataSetChanged(); //데이터가 변경되었음을 알려줌(리스트 새로고침)
    }
}
