import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CrimeMapper
        extends Mapper<LongWritable, Text, Text, IntWritable> {

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();
        String[] words = line.split("\\t", -1);
        String output = "";
        String full_date = "";
        boolean date_found = false;
        for (int i = 0; i < words.length; i++) {
            String[] date = words[i].split("-");
            if (date.length == 3 && !date_found) {
                date_found = true;
                full_date = words[i];
                output = words[i];
            }
            else {
                String[] tweet = words[i].split(" ");
                HashSet<String> keywords = new HashSet<>();
                for (int j = 0; j < tweet.length; j++) {
                    keywords.add(tweet[j].toLowerCase());
                }
                if (keywords.contains("shooting") || keywords.contains("robbery") || keywords.contains("crime") ||
                        keywords.contains("assault") || keywords.contains("armed") || keywords.contains("killed") ||
                        keywords.contains("injured") || keywords.contains("shot") || keywords.contains("arrested") ||
                        keywords.contains("stole") || keywords.contains("stolen") || keywords.contains("gunpoint") ||
                        keywords.contains("police") || keywords.contains("offense") || keywords.contains("offenses")) {
                    output += ", " + words[i];
                }
            }
        }
        if (output.equals(full_date)) {
            return;
        }
        Text outputKey = new Text(output);
        IntWritable outputValue = new IntWritable(1);
        context.write(outputKey, outputValue);
    }
}