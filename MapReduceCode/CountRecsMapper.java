import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CountRecsMapper
        extends Mapper<LongWritable, Text, Text, IntWritable> {

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        if (words[0].equals("id")) {
            return;
        }

        String output = "Total number of tweets: ";
        Text outputKey = new Text(output);
        IntWritable outputValue = new IntWritable(1);
        context.write(outputKey, outputValue);
    }
}