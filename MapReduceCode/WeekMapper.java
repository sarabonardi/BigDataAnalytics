import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class WeekMapper
        extends Mapper<LongWritable, Text, Text, IntWritable> {

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        String line = value.toString();
        String[] words = line.split("\\t", -1);

        try {
            String startDate = words[0];
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = (Date)formatter.parse(startDate);
            Date beg = (Date)formatter.parse("2020-01-01");
            Date end = (Date)formatter.parse("2020-01-04");

            if (date.equals(beg) || date.equals(end) || (date.after(beg) && date.before(end))) {
                String output = "Week of: " + formatter.format(beg);
                Text outputKey = new Text(output);
                int sum = Integer.parseInt(words[1]);
                IntWritable outputValue = new IntWritable(sum);
                context.write(outputKey, outputValue);
            }

            beg = (Date)formatter.parse("2020-01-05");
            end = (Date)formatter.parse("2020-01-11");

            for (int i = 0; i < 52; i++) {
                if (date.equals(beg) || date.equals(end) || (date.after(beg) && date.before(end))) {
                    String output = "Week of: " + formatter.format(beg);
                    Text outputKey = new Text(output);
                    int sum = Integer.parseInt(words[1]);
                    IntWritable outputValue = new IntWritable(sum);
                    context.write(outputKey, outputValue);
                }
                Calendar c = Calendar.getInstance();
                c.setTime(beg);
                c.add(Calendar.DATE, 7);
                String current = formatter.format(c.getTime());
                beg = (Date)formatter.parse(current);

                c.setTime(end);
                c.add(Calendar.DATE, 7);
                current = formatter.format(c.getTime());
                end = (Date)formatter.parse(current);
            }

        } catch (Exception e) {}
    }
}