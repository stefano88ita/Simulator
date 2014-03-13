%launch the_good_filter_beta and get U2 and good_rounds
res=1;
res2=1;
clear res;
clear res2;
clear Afiltered;
%find users that
%for i=1:1861 %number_of_user
%    if(sum(Ulast_g(:)==i)>8) %limit
%        res(i)=1;
%        for j=1:10000
%           if(Ulast_g(j)==i)
%               res2(j)=1;
%           end
%        end
%    end
%end

fid=fopen('filtered_lastfm_expanded.txt', 'wt'); 
j=1;
for i=1:size(good_rounds,1)
    fprintf(fid, '%s', strcat('t#',num2str(i),',u#', num2str(U2(good_rounds(i)))));
    for j=1:size(Xlast_g_big(1,1,:),3)
        for j=1:25
          fprintf(fid, '%s', strcat(',a#',num2str(1),'>')); %action's id does't matter
          for k=1:25
              fprintf(fid, '%s', strcat(num2str(k),':',num2str(Xlast_g_big(good_rounds(i),j,k))));
              if(k<25)
                  fprintf(fid, '%s', ' ');
              else
                 fprintf(fid, '%s', strcat('>',num2str(Ylast_g_bin_big(good_rounds(i),j)))); 
              end
          end
        end
    end
    fprintf(fid, '\n');
end
fclose(fid);

