package org.ivmlab.proloop.proloop.bluesourceteam;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.ivmlab.proloop.proloop.R;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private List<TeamMember> teamMembers;
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        teamMembers = initTeam();

        list = (ListView) findViewById(R.id.activity_list_list);
        list.setAdapter(new ArrayAdapter<TeamMember>(this, R.layout.cell_team) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TeamMember curr = teamMembers.get(position);
                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.cell_team, null);
                }

                ((ImageView) convertView.findViewById(R.id.cell_team_image)).setImageResource(curr.getImageId());
                ((TextView) convertView.findViewById(R.id.cell_team_name)).setText(curr.getName());

                return convertView;
            }

            @Override
            public int getCount() {
                return teamMembers.size();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(ListActivity.this, DetailActivity2.class);
                i.putExtra(DetailActivity2.PARAM_MEMBER, teamMembers.get(position));

                if(getResources().getBoolean(R.bool.animations_enabled)) {
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(ListActivity.this,
                                    Pair.create(view.findViewById(R.id.cell_team_name), "transition_detail_name"),
                                    Pair.create(view.findViewById(R.id.cell_team_image), "transition_detail_image"),
                                    Pair.create(view.findViewById(R.id.cell_team_seperator), "transition_detail_separator"));

                    startActivity(i, options.toBundle());
                } else {
                    startActivity(i);
                }
            }
        });
    }

    private List<TeamMember> initTeam() {
        ArrayList<TeamMember> team = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            team.add(new TeamMember(getString(R.string.team_sansa_name), R.drawable.team_sansa, getString(R.string.team_sansa_description)));
        }
        return team;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_list, menu);
        return true;
    }


}
