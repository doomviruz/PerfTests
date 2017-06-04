function [ ] = Main( )
	Run();
end

function Run()
	[lg, ug] = FillPopulation();
	[var, val] = FillSignals();
	total = 0;
	start = tic();
	
	for generation=0:999
		total = total + CalculateFF(var, val, lg, ug, rem(generation, 2));
	end
	
	timeSpan = toc(start);
	fprintf('%d, %.2fs\n', total, timeSpan);
end

function [result]  = CalculateFF(var, val, lg, ug, valueFactor)
	if valueFactor == 0
		result = 0;
	else
		ss = zeros(1, size(lg,2));
		for i=1:size(lg,1)
			lg_ = sum(var <= lg(i,:),2);
			ug_ = sum(var >= ug(i,:),2);
			ss(i) = val*(lg_+ug_);
		end
		result = sum(ss);
	end
end

function [var, val]  = FillSignals()
	varCount = 32;
	var = zeros(100000, varCount);
	for i=1:100000
		var(i,:) = i+1:varCount+i;
	end
	val = zeros(1,100000);
	val(1:2:100000) = 1;
	val(2:2:100000) = -1;
end

function [lg, ug] = FillPopulation()
	varCount = 32;
	magic = 100031;
	lg = zeros(50, varCount);
	ug = zeros(50, varCount);
	lg(2:2:50,:) = magic/2;
	ug(2:2:50,:) = magic/2;
	lg(1:2:50,:) = 0;
	ug(1:2:50,:) = magic;
end
